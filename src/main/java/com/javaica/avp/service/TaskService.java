package com.javaica.avp.service;

import com.javaica.avp.entity.TaskBlockEntity;
import com.javaica.avp.entity.TaskEntity;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.StageRepository;
import com.javaica.avp.repository.TaskBlockRepository;
import com.javaica.avp.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class TaskService {

    private final AccessService accessService;
    private final TaskRepository taskRepository;
    private final TaskBlockRepository taskBlockRepository;
    private final StageRepository stageRepository;
    private final SubmissionService submissionService;

    public GradedTask getTaskById(Long taskId, AppUser user) {
        if (!accessService.userHasAccessToTask(taskId, user))
            throw new ForbiddenException("User doesn't have access to the task");
        return taskRepository
                .findById(taskId)
                .map(this::mapTaskEntityToGradedModel)
                .orElseThrow(() -> new NotFoundException("Task " + taskId + " not found"));
    }

    public List<GradedTask> getGradedTasksByStageId(long stageId) {
        return taskRepository
                .findAllByStageIdOrderByIndex(stageId)
                .stream()
                .map(this::mapTaskEntityToGradedModel)
                .collect(Collectors.toList());
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
        taskRepository.saveAll(
                taskEntities.stream()
                        .filter(task -> task.getIndex() >= index)
                        .map(task -> task.withIndex(task.getIndex() + 1))
                        .collect(Collectors.toList())
        );
        TaskEntity taskEntity = taskRepository.save(mapTaskRequestToEntity(taskRequest).withIndex(index));
        IntStream.range(0, taskRequest.getBlocks().size())
                .mapToObj(blockIndex -> mapTaskBlockRequestToEntity(
                            taskEntity.getId(),
                            taskRequest.getBlocks().get(blockIndex))
                        .withIndex(blockIndex))
                .forEach(taskBlockRepository::save);
        return mapTaskEntityToModel(taskEntity);
    }

    public void deleteTask(long taskId) {
        taskRepository.deleteById(taskId);
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
                                .findAllByTaskIdOrderByIndex(taskEntity.getId())
                                .stream().map(this::mapTaskBlockEntityToModel)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private GradedTask mapTaskEntityToGradedModel(TaskEntity taskEntity) {
        return GradedTask.builder()
                .id(taskEntity.getId())
                .stageId(taskEntity.getStageId())
                .name(taskEntity.getName())
                .description(taskEntity.getDescription())
                .index(taskEntity.getIndex())
                .blocks(
                        taskBlockRepository
                                .findAllByTaskIdOrderByIndex(taskEntity.getId())
                                .stream().map(this::mapTaskBlockEntityToModel)
                                .collect(Collectors.toList())
                )
                .points(submissionService.getTaskPoints(taskEntity.getId()))
                .build();
    }

    private TaskBlock mapTaskBlockEntityToModel(TaskBlockEntity taskBlockEntity) {
        return TaskBlock.builder()
                .id(taskBlockEntity.getId())
                .content(taskBlockEntity.getContent())
                .type(taskBlockEntity.getType())
                .index(taskBlockEntity.getIndex())
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
