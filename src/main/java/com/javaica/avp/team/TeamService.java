package com.javaica.avp.team;

import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.group.GroupRepository;
import com.javaica.avp.user.AppUser;
import com.javaica.avp.user.UserEntity;
import com.javaica.avp.user.UserRepository;
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

    public void deleteTeam(long teamId) {
        teamRepository.deleteById(teamId);
    }

    public Optional<Team> getTeam(long id) {
        return teamRepository.findById(id).map(this::entityToModel);
    }

    @Transactional
    public Team addUserToTeam(Long teamId, String username) {
        getTeam(teamId).orElseThrow(NotFoundException::new);
        userRepository.save(
                userRepository.findByUsername(username)
                        .orElseThrow(NotFoundException::new)
                        .withTeamId(teamId)
        );
        return getTeam(teamId).orElseThrow(NotFoundException::new);
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

    public boolean teamExists(long id) {
        return teamRepository.existsById(id);
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
