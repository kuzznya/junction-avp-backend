package com.javaica.avp.service;

import com.javaica.avp.entity.TaskBlockEntity;
import com.javaica.avp.entity.TaskEntity;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.Task;
import com.javaica.avp.model.TaskBlock;
import com.javaica.avp.model.TaskBlockRequest;
import com.javaica.avp.model.TaskRequest;
import com.javaica.avp.repository.StageRepository;
import com.javaica.avp.repository.TaskBlockRepository;
import com.javaica.avp.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskBlockRepository taskBlockRepository;
    private final StageRepository stageRepository;

    public Task getTaskById(Long taskId) {
        return taskRepository
                .findById(taskId)
                .map(this::mapTaskEntityToModel)
                .orElseThrow(() -> new NotFoundException("Task " + taskId + " not found"));
    }

    @Transactional
    public Task saveTask(TaskRequest taskRequest) {
        if (!stageRepository.existsById(taskRequest.getStageId()))
            throw new NotFoundException("Stage " + taskRequest.getStageId() + " not found");
        List<TaskEntity> taskEntities = taskRepository.findAllByStageIdOrderByIndex(taskRequest.getStageId());
        Integer index = Optional
                .ofNullable(taskRequest.getIndex())
                .orElseGet(() -> taskEntities.stream()
                        .mapToInt(TaskEntity::getIndex)
                        .max()
                        .orElse(0));
        taskEntities.stream()
                .filter(task -> task.getIndex() >= index)
                .forEach(task -> taskRepository.save(task.withIndex(task.getIndex() + 1)));
        TaskEntity taskEntity = taskRepository.save(mapTaskRequestToEntity(taskRequest).withIndex(index));
        taskRequest.getBlocks()
                .stream()
                .map(block -> mapTaskBlockRequestToEntity(taskEntity.getId(), block))
                .forEach(taskBlockRepository::save);
        return mapTaskEntityToModel(taskEntity);
    }

    private Task mapTaskEntityToModel(TaskEntity taskEntity) {
        return Task.builder()
                .id(taskEntity.getId())
                .stageId(taskEntity.getStageId())
                .name(taskEntity.getName())
                .description(taskEntity.getDescription())
                .index(taskEntity.getIndex())
                .blocks(
                        taskBlockRepository
                                .findAllByTaskId(taskEntity.getId())
                                .stream().map(this::mapTaskBlockEntityToModel)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private TaskBlock mapTaskBlockEntityToModel(TaskBlockEntity taskBlockEntity) {
        return TaskBlock.builder()
                .content(taskBlockEntity.getContent())
                .type(taskBlockEntity.getType())
                .build();
    }

    private TaskEntity mapTaskRequestToEntity(TaskRequest taskRequest) {
        return TaskEntity.builder()
                .stageId(taskRequest.getStageId())
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .index(taskRequest.getIndex())
                .build();
    }

    private TaskBlockEntity mapTaskBlockRequestToEntity(Long taskId, TaskBlockRequest taskBlockRequest) {
        return TaskBlockEntity.builder()
                .taskId(taskId)
                .content(taskBlockRequest.getContent())
                .type(taskBlockRequest.getType())
                .answer(taskBlockRequest.getAnswer())
                .build();
    }
}
