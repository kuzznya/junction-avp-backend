package com.javaica.avp.service;

import com.javaica.avp.entity.BattleEntity;
import com.javaica.avp.entity.GradedTeamProjection;
import com.javaica.avp.entity.TeamEntity;
import com.javaica.avp.exception.BadRequestException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.BattleRepository;
import com.javaica.avp.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final CourseService courseService;
    private final AccessService accessService;
    private final CheckpointService checkpointService;
    private final StageService stageService;

    public List<Battle> getAllBattles() {
        return StreamSupport.stream(battleRepository.findAll().spliterator(), false)
                .map(this::mapBattleEntityToModel)
                .collect(Collectors.toList());
    }

    public Battle getTeamBattles(long teamId) {
        return battleRepository.findByInitiatorIdOrDefenderId(teamId, teamId)
                .map(this::mapBattleEntityToModel)
                .orElseThrow(NotFoundException::new);
    }

    public Battle getUserBattle(AppUser user) {
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("No team found for user " + user.getUsername()));
        return battleRepository
                .findByInitiatorIdOrDefenderId(userTeam.getId(), userTeam.getId())
                .map(this::mapBattleEntityToModel)
                .orElseThrow(NotFoundException::new);
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

    public Battle initiateBattle(Long opponentTeamId, Long checkpointId, AppUser user) {
        if (!accessService.userHasAccessToCheckpoint(checkpointId, user))
            throw new AccessDeniedException("Cannot access checkpoint " + checkpointId);
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("User does not belong to any team"));
        if (userTeam.getId().equals(opponentTeamId))
            throw new AccessDeniedException("Cannot initiate battle with the same team");
        if (!teamRepository.existsById(opponentTeamId))
            throw new NotFoundException("Team " + opponentTeamId + " not found");
        if (!courseService.getTeamCourse(opponentTeamId).getId()
                .equals(courseService.getTeamCourse(userTeam.getId()).getId()))
            throw new BadRequestException("Teams are from different courses");
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
        return setBattleStatus(battleId, user, BattleStatus.ACCEPTED);
    }

    public Battle declineBattle(Long battleId, AppUser user) {
        return setBattleStatus(battleId, user, BattleStatus.DECLINED);
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
        if (!teamService
                .getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("Team for user " + user.getUsername() + " not found"))
                .getId().equals(battle.getDefenderId())) {
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
                        teamRepository.findById(battleEntity.getInitiatorId())
                                .map(this::teamToHeader)
                                .orElse(null)
                )
                .defender(teamRepository.findById(battleEntity.getDefenderId())
                        .map(this::teamToHeader)
                        .orElse(null))
                .checkpointId(battleEntity.getCheckpointId())
                .build();
    }

    private TeamHeader teamToHeader(TeamEntity team) {
        return new TeamHeader(
                team.getId(),
                team.getName()
        );
    }

    public BattleProgress getBattleProgress(Long battleId) {
        BattleEntity battleEntity = battleRepository.findById(battleId)
                .orElseThrow(() -> new NotFoundException("Battle " + battleId + " not found"));
        return BattleProgress.builder()
                .battle(mapBattleEntityToModel(battleEntity))
                .initiatorProgress(getTeamProgress(battleEntity.getInitiatorId(), battleEntity.getCheckpointId()))
                .defenderProgress(getTeamProgress(battleEntity.getDefenderId(), battleEntity.getCheckpointId()))
                .build();
    }

    public BattleProgress getCurrentBattleProgress(AppUser user) {
        return getBattleProgress(getUserBattle(user).getId());
    }

    private List<StageProgress> getTeamProgress(long teamId, long targetCheckpointId) {
        long courseId = courseService.getTeamCourse(teamId).getId();
        return stageService.getStagesByCourse(courseId, teamId)
                .stream()
                .sorted(Comparator.comparingInt(Stage::getIndex))
                .takeWhile(stage -> stage.getCheckpoint().getId().equals(targetCheckpointId))
                .map(stage -> new StageProgress(
                        stage.getName(),
                        Optional.ofNullable(stage.getCheckpoint())
                                .map(checkpoint -> Optional.ofNullable(checkpoint.getStatus())
                                        .map(status -> status.equals(CheckpointSubmissionStatus.ACCEPTED))
                                        .orElse(false))
                                .orElse(false)))
                .collect(Collectors.toList());
    }
}
