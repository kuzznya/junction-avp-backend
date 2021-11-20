package com.javaica.avp.service;

import com.javaica.avp.entity.StageEntity;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.CheckpointRepository;
import com.javaica.avp.repository.StageRepository;
import com.javaica.avp.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StageService {

    private StageRepository stageRepository;
    private TaskRepository taskRepository;
    private CheckpointRepository checkpointRepository;

    public List<StageHeader> getStageHeaders(Long courseId) {
        return stageRepository.findAllHeadersByCourseId(courseId);
    }

    public Stage getStageById(Long stageId) {
        return mapStageEntityToModel(
                stageRepository
                        .findById(stageId)
                        .orElseThrow(() -> new NotFoundException("Stage " + stageId + "not found"))
        );
    }

    @Transactional
    public Stage saveStage(StageRequest stage) {
        return mapStageEntityToModel(stageRepository.save(mapStageModelToEntity(stage)));
    }

    private Stage mapStageEntityToModel(StageEntity stageEntity) {
        return Stage.builder()
                .name(stageEntity.getName())
                .description(stageEntity.getDescription())
                .tasks(
                        taskRepository.findAllByStageId(stageEntity.getId())
                                .stream()
                                .map(task -> TaskHeader.builder()
                                        .id(task.getId())
                                        .name(task.getName())
                                        .description(task.getDescription())
                                        .build())
                                .collect(Collectors.toList())
                )
                .checkpoint(
                        checkpointRepository.findByStageId(stageEntity.getId())
                                .map(checkpoint -> CheckpointHeader.builder()
                                        .id(checkpoint.getId())
                                        .name(checkpoint.getName())
                                        .description(checkpoint.getDescription()).build())
                                .orElse(null)
                )
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
