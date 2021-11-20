package com.javaica.avp.service;

import com.javaica.avp.entity.BattleEntity;
import com.javaica.avp.entity.GradedTeamProjection;
import com.javaica.avp.exception.BadRequestException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Battle;
import com.javaica.avp.model.BattleStatus;
import com.javaica.avp.repository.BattleRepository;
import com.javaica.avp.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;
    private final CourseService courseService;

    public List<Battle> getUserBattles(AppUser user) {
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("No team found for user " + user.getUsername()));
        return battleRepository
                .findAllByInitiatorIdOrDefenderId(userTeam.getId(), userTeam.getId())
                .stream()
                .map(this::mapBattleEntityToModel)
                .collect(Collectors.toList());
    }

    public Battle getBattleById(Long battleId, AppUser user) {
        Battle battle = battleRepository.findById(battleId)
                .map(this::mapBattleEntityToModel)
                .orElseThrow(() -> new NotFoundException("Battle " + battleId + " not found"));
        GradedTeamProjection team = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new AccessDeniedException("No team found for user " + user.getUsername()));

        if (!battle.getDefenderId().equals(team.getId()) || !battle.getInitiatorId().equals(team.getId()))
            throw new AccessDeniedException("Cannot access battle " + battleId);
        return battle;
    }

    public Battle initiateBattle(Long opponentTeamId, AppUser user) {
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
        return mapBattleEntityToModel(battleRepository.save(battle.withStatus(status)));
    }

    private Battle mapBattleEntityToModel(BattleEntity battleEntity) {
        return Battle
                .builder()
                .status(battleEntity.getStatus())
                .initiatorId(battleEntity.getInitiatorId())
                .defenderId(battleEntity.getDefenderId())
                .build();
    }
}
