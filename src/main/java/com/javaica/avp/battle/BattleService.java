package com.javaica.avp.battle;

import com.javaica.avp.course.CourseService;
import com.javaica.avp.exception.BadRequestException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.security.AccessService;
import com.javaica.avp.stage.StageProgress;
import com.javaica.avp.stage.StageService;
import com.javaica.avp.submission.SubmissionService;
import com.javaica.avp.submission.checkpoint.CheckpointSubmissionResult;
import com.javaica.avp.submission.checkpoint.CheckpointSubmissionStatus;
import com.javaica.avp.team.*;
import com.javaica.avp.user.AppUser;
import com.javaica.avp.user.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class BattleService {

    private final TeamService teamService;
    private final CourseService courseService;
    private final AccessService accessService;
    private final StageService stageService;
    private final SubmissionService submissionService;

    private final BattleRepository battleRepository;

    private final ApplicationEventPublisher eventPublisher;

    public List<Battle> getAllBattles() {
        return StreamSupport.stream(battleRepository.findAll().spliterator(), false)
                .map(this::mapBattleEntityToModel)
                .collect(Collectors.toList());
    }

    public List<Battle> getTeamBattles(long teamId) {
        return battleRepository
                .findAllByInitiatorIdOrDefenderId(teamId, teamId).stream()
                .map(this::mapBattleEntityToModel)
                .collect(Collectors.toList());
    }

    public Battle getUserBattle(AppUser user) {
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("No team found for user " + user.getUsername()));
        return battleRepository
                .findAllByInitiatorIdOrDefenderId(userTeam.getId(), userTeam.getId()).stream()
                .filter(battleEntity -> battleEntity.getStatus().equals(BattleStatus.PENDING) ||
                        battleEntity.getStatus().equals(BattleStatus.ACCEPTED))
                .findAny()
                .map(this::mapBattleEntityToModel)
                .orElseThrow(NotFoundException::new);
    }

    public BattleProgress getBattleProgress(Long battleId, AppUser user) {
        BattleEntity battleEntity = battleRepository.findById(battleId)
                .orElseThrow(() -> new NotFoundException("Battle " + battleId + " not found"));
        BattleProgress.UserTeamRole userTeamRole = user.getTeamId().equals(battleEntity.getInitiatorId()) ?
                BattleProgress.UserTeamRole.INITIATOR :
                BattleProgress.UserTeamRole.DEFENDER;
        return BattleProgress.builder()
                .battle(mapBattleEntityToModel(battleEntity))
                .initiatorProgress(getTeamProgress(battleEntity.getInitiatorId(), battleEntity.getCheckpointId()))
                .defenderProgress(getTeamProgress(battleEntity.getDefenderId(), battleEntity.getCheckpointId()))
                .userTeamRole(userTeamRole)
                .build();
    }

    public BattleProgress getCurrentBattleProgress(AppUser user) {
        return getBattleProgress(getUserBattle(user).getId(), user);
    }

    public Battle getBattleById(Long battleId, AppUser user) {
        Battle battle = battleRepository.findById(battleId)
                .map(this::mapBattleEntityToModel)
                .orElseThrow(() -> new NotFoundException("Battle " + battleId + " not found"));
        if (user.getRole() == UserRole.ADMIN)
            return battle;
        GradedTeamProjection team = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("No team found for user " + user.getUsername()));

        if (!battle.getDefender().getId().equals(team.getId()) || !battle.getInitiator().getId().equals(team.getId()))
            throw new AccessDeniedException("Cannot access battle " + battleId);
        return battle;
    }

    @EventListener
    public void onSubmissionReview(CheckpointSubmissionResult result) {
        Optional<Battle> battleOptional = battleRepository.findActiveBattleByTeamId(result.getTeamId()).map(this::mapBattleEntityToModel);
        if (battleOptional.isEmpty())
            return;
        Battle battle = battleOptional.get();
        if (!Objects.equals(battle.getCheckpointId(), result.getCheckpointId()))
            return;
        TeamHeader opponent = battle.getInitiator().getId().equals(result.getTeamId()) ? battle.getDefender() : battle.getInitiator();
        Optional<CheckpointSubmissionResult> opponentSubmission = submissionService.getSubmissionOfTeam(result.getCheckpointId(), opponent.getId());
        if (opponentSubmission.isEmpty()) {
            onBattleWon(battle, result.getId());
        } else {
            CheckpointSubmissionResult opponentResult = opponentSubmission.get();
            if (opponentResult.getStatus() == CheckpointSubmissionStatus.DECLINED ||
                    opponentResult.getStatus() == CheckpointSubmissionStatus.NEW) {
                onBattleWon(battle, result.getId());
            }
            if (opponentResult.getStatus() == CheckpointSubmissionStatus.IN_REVIEW)
                return;
            long winnerId = result.getSubmissionTimestamp().isBefore(opponentResult.getSubmissionTimestamp()) ? result.getId() : opponentResult.getId();
            onBattleWon(battle, winnerId);
        }
    }

    @Transactional
    public Battle initiateBattle(Long opponentTeamId, Long checkpointId, AppUser user) {
        if (!accessService.userHasAccessToCheckpoint(checkpointId, user))
            throw new AccessDeniedException("Cannot access checkpoint " + checkpointId);
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("User does not belong to any team"));
        if (userTeam.getId().equals(opponentTeamId))
            throw new AccessDeniedException("Cannot initiate battle with the same team");
        if (!teamService.teamExists(opponentTeamId))
            throw new NotFoundException("Team " + opponentTeamId + " not found");
        if (!courseService.getTeamCourse(opponentTeamId).getId()
                .equals(courseService.getTeamCourse(userTeam.getId()).getId()))
            throw new BadRequestException("Teams are from different courses");
        if (isTeamParticipatingInBattle(user.getTeamId()))
            throw new BadRequestException("Team " + userTeam.getId() + " is already participating in battle");
        if (isTeamParticipatingInBattle(opponentTeamId))
            throw new BadRequestException("Team " + opponentTeamId + " is already participating in battle");
        log.debug("Neither {} nor {} is already participating in battle", userTeam.getId(), opponentTeamId);
        return mapBattleEntityToModel(
                battleRepository.save(
                        BattleEntity.builder()
                                .status(BattleStatus.PENDING)
                                .initiatorId(user.getTeamId())
                                .defenderId(opponentTeamId)
                                .checkpointId(checkpointId)
                                .build()
                )
        );
    }

    public Battle acceptBattle(Long battleId, AppUser user) {
        log.info("Battle {} accepted", battleId);
        return setBattleStatus(battleId, user, BattleStatus.ACCEPTED);
    }

    public Battle declineBattle(Long battleId, AppUser user) {
        log.info("Battle {} declined", battleId);
        return setBattleStatus(battleId, user, BattleStatus.DECLINED);
    }

    private void onBattleWon(Battle battle, long submissionId) {
        BattleEntity entity = battleRepository.findById(battle.getId())
                .orElseThrow(() -> new NotFoundException("Battle " + battle.getId() + " not found"));
        battleRepository.save(entity.withStatus(BattleStatus.FINISHED));
        log.info("Battle {} finished by submission {}", battle.getId(), submissionId);
        eventPublisher.publishEvent(new BattleWonEvent(submissionId));
    }

    private Battle setBattleStatus(Long battleId,
                                   AppUser user,
                                   BattleStatus status) {
        BattleEntity battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new NotFoundException("Battle " + battleId + " not found"));
        if (!battle.getStatus().equals(BattleStatus.PENDING))
            throw new AccessDeniedException(
                    "Cannot set " + status.toString().toLowerCase() +
                            " status, battle is already " + battle.getStatus().toString().toLowerCase());
        if (!user.getTeamId().equals(battle.getDefenderId())) {
            throw new AccessDeniedException("Cannot accept battle");
        }
        return mapBattleEntityToModel(
                battleRepository.save(battle.withStatus(status)));
    }

    private Battle mapBattleEntityToModel(BattleEntity battleEntity) {
        return Battle
                .builder()
                .id(battleEntity.getId())
                .status(battleEntity.getStatus())
                .initiator(
                        teamService.getTeam(battleEntity.getInitiatorId())
                                .map(this::teamToHeader)
                                .orElse(null)
                )
                .defender(teamService.getTeam(battleEntity.getDefenderId())
                        .map(this::teamToHeader)
                        .orElse(null))
                .checkpointId(battleEntity.getCheckpointId())
                .build();
    }

    private TeamHeader teamToHeader(Team team) {
        return new TeamHeader(
                team.getId(),
                team.getName()
        );
    }

    private boolean isTeamParticipatingInBattle(long teamId) {
        return battleRepository.findActiveBattleByTeamId(teamId).isPresent();
    }

    private List<StageProgress> getTeamProgress(long teamId, long targetCheckpointId) {
        long courseId = courseService.getTeamCourse(teamId).getId();
        AtomicBoolean checkpointFound = new AtomicBoolean(false);
        return stageService.getStagesByCourse(courseId, teamId)
                .stream()
                .takeWhile(stage -> !checkpointFound.get())
                .peek(stage -> checkpointFound.set(stage.getCheckpoint().getId().equals(targetCheckpointId)))
                .map(stage -> new StageProgress(
                        stage.getName(),
                        Optional.ofNullable(stage.getCheckpoint().getStatus())
                                .map(status -> status.equals(CheckpointSubmissionStatus.ACCEPTED))
                                .orElse(false))
                )
                .collect(Collectors.toList());
    }
}
