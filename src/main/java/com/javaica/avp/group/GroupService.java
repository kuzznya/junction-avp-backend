package com.javaica.avp.group;

import com.javaica.avp.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    public Group createGroup(Group group) {
        GroupEntity entity = modelToEntity(group.withId(null));
        return entityToModel(repository.save(entity));
    }

    public void deleteGroup(long groupId) {
        repository.deleteById(groupId);
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

    private GroupEntity modelToEntity(Group group) {
        return GroupEntity.builder()
                .id(group.getId())
                .courseId(group.getCourseId())
                .complexityLevel(group.getComplexityLevel())
                .build();
    }
}
