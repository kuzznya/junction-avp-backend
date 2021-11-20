package com.javaica.avp.service;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.repository.CourseRepository;
import com.javaica.avp.repository.StageRepository;
import com.javaica.avp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final CourseRepository courseRepository;
    private final StageRepository stageRepository;
    private final TaskRepository taskRepository;

    public boolean userHasAccessToCourse(long courseId, AppUser user) {
        if (user.getTeamId() == null)
            return false;
        return courseRepository.courseExistsWithTeamId(courseId, user.getTeamId());
    }

    public boolean userHasAccessToStage(long stageId, AppUser user) {
        return stageRepository.findById(stageId)
                .map(stage -> userHasAccessToCourse(stage.getCourseId(), user))
                .orElse(false);
    }

    public boolean userHasAccessToTask(long taskId, AppUser user) {
        if (user.getTeamId() == null)
            return false;
        return taskRepository.existsByTeamId(taskId, user.getTeamId());
    }
}