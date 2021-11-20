package com.javaica.avp.service;

import com.javaica.avp.entity.GroupEntity;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Group;
import com.javaica.avp.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    public Group createGroup(long courseId, Group group) {
        GroupEntity entity = modelToEntity(group.withId(null), courseId);
        return entityToModel(repository.save(entity));
    }

    public List<Group> getAllGroups(long courseId) {
        return repository.findAllByCourseId(courseId).stream()
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    public Optional<Group> getCurrentGroup(AppUser user) {
        return repository.findByTeamId(user.getTeamId())
                .map(this::entityToModel);
    }

    private Group entityToModel(GroupEntity entity) {
        return Group.builder()
                .id(entity.getId())
                .complexityLevel(entity.getComplexityLevel())
                .courseId(entity.getCourseId())
                .build();
    }

    private GroupEntity modelToEntity(Group group, long courseId) {
        return GroupEntity.builder()
                .id(group.getId())
                .courseId(courseId)
                .complexityLevel(group.getComplexityLevel())
                .build();
    }
}
