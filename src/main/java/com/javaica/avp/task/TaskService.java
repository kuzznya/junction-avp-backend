package com.javaica.avp.task;

import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.security.AccessService;
import com.javaica.avp.stage.StageRepository;
import com.javaica.avp.submission.SubmissionService;
import com.javaica.avp.task.entity.TaskBlockEntity;
import com.javaica.avp.task.entity.TaskEntity;
import com.javaica.avp.task.model.*;
import com.javaica.avp.user.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
                .orElseGet(() -> {
                    var maxIndex = taskEntities.stream()
                            .mapToInt(TaskEntity::getIndex)
                            .max();
                    return maxIndex.isPresent() ? maxIndex.getAsInt() + 1 : 0;
                });
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
        Optional<TaskEntity> taskOptional = taskRepository.findById(taskId);
        taskRepository.deleteById(taskId);
        if (taskOptional.isEmpty())
            return;
        AtomicInteger idx = new AtomicInteger(0);
        List<TaskEntity> tasks = taskRepository.findAllByStageIdOrderByIndex(taskOptional.get().getStageId())
                .stream()
                .map(task -> task.withIndex(idx.getAndIncrement()))
                .collect(Collectors.toList());
        taskRepository.saveAll(tasks);
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
