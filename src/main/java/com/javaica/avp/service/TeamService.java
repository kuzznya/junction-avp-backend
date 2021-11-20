package com.javaica.avp.service;

import com.javaica.avp.entity.GradedTeamProjection;
import com.javaica.avp.entity.TeamEntity;
import com.javaica.avp.entity.UserEntity;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Team;
import com.javaica.avp.repository.GroupRepository;
import com.javaica.avp.repository.TeamRepository;
import com.javaica.avp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public Team createTeam(Team team) {
        if (!groupRepository.existsById(team.getGroupId()))
            throw new NotFoundException("Group with id " + team.getGroupId() + " not found");
        TeamEntity entity = modelToEntity(team.getGroupId(), team.withId(null));
        TeamEntity savedEntity = teamRepository.save(entity);
        for (String username : team.getMembers()) {
            userRepository.findByUsername(username)
                    .map(user -> user.withTeamId(savedEntity.getId()))
                    .ifPresent(userRepository::save);
        }
        return entityToModel(savedEntity);
    }

    public Optional<Team> getTeam(long id) {
        return teamRepository.findById(id).map(this::entityToModel);
    }

    public Optional<GradedTeamProjection> getTeamOfUser(AppUser user) {
        return Optional.ofNullable(user.getTeamId())
                .flatMap(teamRepository::findByIdWithPoints);
    }

    public List<GradedTeamProjection> getLeaderboard(long teamId) {
        if (!teamRepository.existsById(teamId))
            throw new NotFoundException("Team with id " + teamId + " not found");
        return teamRepository.getLeaderboard(teamId);
    }

    public List<GradedTeamProjection> getLeaderboard(AppUser user) {
        if (user.getTeamId() == null)
            throw new ForbiddenException("User has no team");
        return teamRepository.getLeaderboard(user.getTeamId());
    }

    public List<Team> getAllTeams(long groupId) {
        return teamRepository.findAllByGroupId(groupId).stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    private TeamEntity modelToEntity(long groupId, Team team) {
        return TeamEntity.builder()
                .id(team.getId())
                .name(team.getName())
                .groupId(groupId)
                .build();
    }

    private Team entityToModel(TeamEntity entity) {
        return Team.builder()
                .id(entity.getId())
                .groupId(entity.getGroupId())
                .name(entity.getName())
                .members(getUsernamesByTeamId(entity.getId()))
                .build();
    }

    private List<String> getUsernamesByTeamId(long teamId) {
        return userRepository.findAllByTeamId(teamId)
                .stream()
                .map(UserEntity::getUsername)
                .collect(Collectors.toList());
    }
}
