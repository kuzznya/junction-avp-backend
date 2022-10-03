package com.javaica.avp.security;

import com.javaica.avp.checkpoint.CheckpointRepository;
import com.javaica.avp.course.CourseRepository;
import com.javaica.avp.stage.StageRepository;
import com.javaica.avp.task.TaskRepository;
import com.javaica.avp.user.AppUser;
import com.javaica.avp.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final CourseRepository courseRepository;
    private final StageRepository stageRepository;
    private final TaskRepository taskRepository;
    private final CheckpointRepository checkpointRepository;

    public boolean userHasAccessToCourse(long courseId, AppUser user) {
        if (user.getRole() == UserRole.ADMIN)
            return true;
        if (user.getTeamId() == null)
            return false;
        return courseRepository.courseExistsWithTeamId(courseId, user.getTeamId());
    }

    public boolean userHasAccessToStage(long stageId, AppUser user) {
        if (user.getRole() == UserRole.ADMIN)
            return true;
        return stageRepository.findById(stageId)
                .map(stage -> userHasAccessToCourse(stage.getCourseId(), user))
                .orElse(false);
    }

    public boolean userHasAccessToTask(long taskId, AppUser user) {
        if (user.getRole() == UserRole.ADMIN)
            return true;
        if (user.getTeamId() == null)
            return false;
        return taskRepository.existsByTeamId(taskId, user.getTeamId());
    }

    public boolean userHasAccessToCheckpoint(long checkpointId, AppUser user) {
        if (user.getRole() == UserRole.ADMIN)
            return true;
        if (user.getTeamId() == null)
            return false;
        return checkpointRepository
                .findById(checkpointId)
                .map(checkpoint -> userHasAccessToStage(checkpoint.getStageId(), user))
                .orElse(false);
    }
}
