package com.javaica.avp.service;

import com.javaica.avp.entity.StageEntity;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.CourseRepository;
import com.javaica.avp.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StageService {

    private final AccessService accessService;
    private final CourseService courseService;
    private final StageRepository stageRepository;
    private final CourseRepository courseRepository;
    private final TaskService taskService;
    private final CheckpointService checkpointService;

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
        return mapStageEntityToModelForUser(stage, user);
    }

    @Transactional
    public Stage saveStage(StageRequest stage) {
        if (!courseRepository.existsById(stage.getCourseId()))
            throw new NotFoundException("Course " + stage.getCourseId() + " not found");
        return mapStageEntityToModel(stageRepository.save(mapStageModelToEntity(stage)));
    }

    private Stage mapStageEntityToModelForUser(StageEntity stageEntity, AppUser user) {
        return Stage.builder()
                .id(stageEntity.getId())
                .name(stageEntity.getName())
                .description(stageEntity.getDescription())
                .tasks(taskService.getGradedTasksByStageId(stageEntity.getId()))
                .checkpoint(checkpointService.getGradedCheckpointByStageId(stageEntity.getId(), user))
                .courseId(stageEntity.getCourseId())
                .build();
    }

    private Stage mapStageEntityToModel(StageEntity stageEntity) {
        return Stage.builder()
                .id(stageEntity.getId())
                .name(stageEntity.getName())
                .description(stageEntity.getDescription())
                .tasks(Collections.emptyList())
                .courseId(stageEntity.getCourseId())
                .build();
    }

    private StageEntity mapStageModelToEntity(StageRequest stage) {
        return StageEntity.builder()
                .courseId(stage.getCourseId())
                .name(stage.getName())
                .description(stage.getDescription())
                .build();
    }
}
