package com.javaica.avp.service;

import com.javaica.avp.entity.StageEntity;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.CheckpointRepository;
import com.javaica.avp.repository.CourseRepository;
import com.javaica.avp.repository.StageRepository;
import com.javaica.avp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StageService {

    private final AccessService accessService;
    private final StageRepository stageRepository;
    private final TaskRepository taskRepository;
    private final CheckpointRepository checkpointRepository;
    private final CourseRepository courseRepository;

    public List<StageHeader> getStageHeaders(long courseId, AppUser user) {
        if (!accessService.userHasAccessToCourse(courseId, user))
            throw new ForbiddenException("User doesn't have access to the course");
        return stageRepository.findAllHeadersByCourseId(courseId);
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

    private Stage mapStageEntityToModel(StageEntity stageEntity) {
        return Stage.builder()
                .id(stageEntity.getId())
                .name(stageEntity.getName())
                .description(stageEntity.getDescription())
                .tasks(getTaskHeaders(stageEntity))
                .checkpoint(getCheckpoint(stageEntity))
                .courseId(stageEntity.getCourseId())
                .build();
    }

    private Stage mapStageEntityToModelForUser(StageEntity stageEntity, AppUser user) {
        return Stage.builder()
                .id(stageEntity.getId())
                .name(stageEntity.getName())
                .description(stageEntity.getDescription())
                .tasks(getGradedTaskHeaders(stageEntity, user))
                .checkpoint(getGradedCheckpoint(stageEntity, user))
                .courseId(stageEntity.getCourseId())
                .build();
    }

    private List<TaskHeader> getTaskHeaders(StageEntity stageEntity) {
        return taskRepository.findAllByStageIdOrderByIndex(stageEntity.getId())
                .stream()
                .map(task -> TaskHeader.builder()
                        .id(task.getId())
                        .name(task.getName())
                        .description(task.getDescription())
                        .index(task.getIndex())
                        .build())
                .collect(Collectors.toList());
    }

    private List<TaskHeader> getGradedTaskHeaders(StageEntity stageEntity, AppUser user) {
        return taskRepository.findAllByStageIdWithCalculatedPoints(stageEntity.getId(), user.getTeamId())
                .stream()
                .peek(System.out::println)
                .map(header -> (TaskHeader) header)
                .collect(Collectors.toList());
    }

    private CheckpointHeader getCheckpoint(StageEntity stageEntity) {
        return checkpointRepository.findByStageId(stageEntity.getId())
                .map(checkpoint -> CheckpointHeader.builder()
                        .id(checkpoint.getId())
                        .name(checkpoint.getName())
                        .description(checkpoint.getDescription())
                        .build())
                .orElse(null);
    }

    private CheckpointHeader getGradedCheckpoint(StageEntity stageEntity, AppUser user) {
        return checkpointRepository.findByStageIdWithSubmission(stageEntity.getId(), user.getTeamId())
                .map(checkpoint -> GradedCheckpointHeader.builder()
                        .id(checkpoint.getId())
                        .name(checkpoint.getName())
                        .description(checkpoint.getDescription())
                        .status(checkpoint.getStatus())
                        .points(checkpoint.getPoints())
                        .build())
                .orElse(null);
    }

    private StageEntity mapStageModelToEntity(StageRequest stage) {
        return StageEntity.builder()
                .courseId(stage.getCourseId())
                .name(stage.getName())
                .description(stage.getDescription())
                .build();
    }
}
