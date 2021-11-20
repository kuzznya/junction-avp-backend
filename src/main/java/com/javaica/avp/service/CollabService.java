package com.javaica.avp.service;

import com.javaica.avp.entity.CollabEntity;
import com.javaica.avp.entity.GradedTeamProjection;
import com.javaica.avp.entity.TeamEntity;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.CollabRepository;
import com.javaica.avp.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CollabService {

    private final CollabRepository collabRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;

    public Collab getCollabById(long collabId, AppUser user) {

        return collabRepository.findById(collabId)
                .map(this::mapCollabEntityToModel)
                .orElseThrow(() -> new NotFoundException("Collab " + collabId + " not found"));
    }

    public List<Collab> getCurrentCollabs(AppUser user) {
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new ForbiddenException("Team for user " + user.getUsername() + " not found"));
        return collabRepository.findAllByRequesterIdOrHelperId(
                    userTeam.getId(),
                    userTeam.getId())
                .stream()
                .map(this::mapCollabEntityToModel)
                .collect(Collectors.toList());
    }

    public Collab requestCollab(long helperId, AppUser user) {
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new ForbiddenException("Team for user " + user.getUsername() + " not found"));
        TeamEntity helperTeam = teamRepository.findById(helperId)
                .orElseThrow(() -> new NotFoundException("Team " + helperId + " not found"));
        if (userTeam.getId().equals(helperTeam.getId()))
            throw new ForbiddenException("Cannot collaborate with same team");
        return mapCollabEntityToModel(
                collabRepository.save(
                    CollabEntity.builder()
                            .requesterId(userTeam.getId())
                            .helperId(helperId)
                            .status(CollabStatus.PENDING)
                            .build()
                )
        );
    }

    public Collab acceptCollab(long collabId, AppUser user) {
        return setCollabStatus(collabId, user, CollabStatus.ACCEPTED);
    }

    public Collab declineCollab(long collabId, AppUser user) {
        return setCollabStatus(collabId, user, CollabStatus.DECLINED);
    }

    public Collab resolveCollab(long collabId, AppUser user) {
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new ForbiddenException("Team for user " + user.getUsername() + " not found"));
        CollabEntity collabEntity = collabRepository.findById(collabId)
                .orElseThrow(() -> new NotFoundException("Collab " + collabId + " not found"));
        if (!collabEntity.getRequesterId().equals(userTeam.getId())
                || !collabEntity.getStatus().equals(CollabStatus.ACCEPTED))
            throw new ForbiddenException("Cannot resolve collab");
        return mapCollabEntityToModel(
                collabRepository.save(collabEntity.withStatus(CollabStatus.RESOLVED))
        );
    }

    private Collab setCollabStatus(long collabId, AppUser user, CollabStatus status) {
        GradedTeamProjection userTeam = teamService.getTeamOfUser(user)
                .orElseThrow(() -> new ForbiddenException("Team for user " + user.getUsername() + " not found"));
        CollabEntity collabEntity = collabRepository.findById(collabId)
                .orElseThrow(() -> new NotFoundException("Collab " + collabId + " not found"));
        if (!collabEntity.getHelperId().equals(userTeam.getId())
                || !collabEntity.getStatus().equals(CollabStatus.PENDING))
            throw new ForbiddenException("Cannot accept collab");
        return mapCollabEntityToModel(
                collabRepository.save(collabEntity.withStatus(status))
        );
    }

    private Collab mapCollabEntityToModel(CollabEntity entity) {
        return Collab.builder()
                .id(entity.getId())
                .helper(teamRepository.findById(entity.getHelperId())
                        .map(this::teamToHeader)
                        .orElse(null))
                .requester(teamRepository.findById(entity.getRequesterId())
                        .map(this::teamToHeader)
                        .orElse(null))
                .status(entity.getStatus())
                .build();
    }

    private TeamHeader teamToHeader(TeamEntity team) {
        return new TeamHeader(
                team.getId(),
                team.getName()
        );
    }
}
