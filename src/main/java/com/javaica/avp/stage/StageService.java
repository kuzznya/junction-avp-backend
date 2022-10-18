package com.javaica.avp.stage;

import com.javaica.avp.checkpoint.CheckpointService;
import com.javaica.avp.course.CourseRepository;
import com.javaica.avp.course.CourseService;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.security.AccessService;
import com.javaica.avp.task.TaskService;
import com.javaica.avp.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StageService {

    private final AccessService accessService;
    private final CourseService courseService;
    private final TaskService taskService;
    private final CheckpointService checkpointService;

    private final StageRepository stageRepository;
    private final CourseRepository courseRepository;

    public List<StageHeader> getCurrentStageHeaders(AppUser user) {
        return getStageHeaders(courseService.getCurrentCourse(user).getId(), user);
    }

    public List<StageHeader> getStageHeaders(long courseId, AppUser user) {
        if (!accessService.userHasAccessToCourse(courseId, user))
            throw new ForbiddenException("User doesn't have access to the course");
        return stageRepository.findAllHeadersByCourseId(courseId, user.getTeamId());
    }

    public Stage getStageById(long stageId, AppUser user) {
        if (!accessService.userHasAccessToStage(stageId, user))
            throw new ForbiddenException("User doesn't have access to the course");
        StageEntity stage = stageRepository
                .findById(stageId)
                .orElseThrow(() -> new NotFoundException("Stage " + stageId + " not found"));
        return mapStageEntityToModelForUser(stage, user.getTeamId());
    }

    @Transactional
    public Stage saveStage(StageRequest stage) {
        if (!courseRepository.existsById(stage.getCourseId()))
            throw new NotFoundException("Course " + stage.getCourseId() + " not found");

        return mapStageEntityToModel(stageRepository.save(mapStageModelToEntity(stage)));
    }

    public void deleteStage(long stageId) {
        stageRepository.deleteById(stageId);
    }

    public List<Stage> getStagesByCourse(long courseId, long teamId) {
        return stageRepository.findAllByCourseIdOrderByIndex(courseId)
                .stream()
                .map(stage -> mapStageEntityToModelForUser(stage, teamId))
                .collect(Collectors.toList());
    }


    private Stage mapStageEntityToModelForUser(StageEntity stageEntity, long teamId) {
        return Stage.builder()
                .id(stageEntity.getId())
                .name(stageEntity.getName())
                .description(stageEntity.getDescription())
                .tasks(taskService.getGradedTasksByStageId(stageEntity.getId()))
                .checkpoint(checkpointService.getGradedCheckpointByStageId(stageEntity.getId(), teamId))
                .courseId(stageEntity.getCourseId())
                .index(stageEntity.getIndex())
                .build();
    }

    private Stage mapStageEntityToModel(StageEntity stageEntity) {
        return Stage.builder()
                .id(stageEntity.getId())
                .name(stageEntity.getName())
                .description(stageEntity.getDescription())
                .tasks(Collections.emptyList())
                .courseId(stageEntity.getCourseId())
                .index(stageEntity.getIndex())
                .build();
    }

    private StageEntity mapStageModelToEntity(StageRequest stage) {
        return StageEntity.builder()
                .courseId(stage.getCourseId())
                .name(stage.getName())
                .description(stage.getDescription())
                .index(stageRepository
                        .findAllByCourseIdOrderByIndex(stage.getCourseId())
                        .stream().mapToInt(StageEntity::getIndex)
                        .max()
                        .orElse(-1) + 1)
                .build();
    }
}
