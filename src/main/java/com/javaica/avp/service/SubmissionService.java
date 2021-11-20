package com.javaica.avp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.entity.*;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.CheckpointSubmissionStatus;
import com.javaica.avp.model.ContentBlockType;
import com.javaica.avp.model.TaskSubmissionResult;
import com.javaica.avp.repository.*;
import com.javaica.avp.util.JsonNodeAnswerComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

    private final AccessService accessService;
    private final TaskRepository taskRepository;
    private final CheckpointRepository checkpointRepository;
    private final TaskBlockRepository blockRepository;
    private final CheckpointBlockRepository checkpointBlockRepository;
    private final TaskSubmissionRepository taskSubmissionRepository;
    private final TaskSubmissionAnswerRepository taskSubmissionAnswerRepository;
    private final CheckpointSubmissionRepository checkpointSubmissionRepository;
    private final CheckpointSubmissionAnswerRepository checkpointSubmissionAnswerRepository;

    @Transactional
    public TaskSubmissionResult submitTask(long taskId, Map<String, JsonNode> submission, AppUser user) {
        if (!accessService.userHasAccessToTask(taskId, user))
            throw new ForbiddenException("User doesn't have access to the task");
        if (!taskRepository.existsById(taskId))
            throw new NotFoundException("Task with id " + taskId + " not found");
        if (taskSubmissionRepository.existsByTaskIdAndTeamId(taskId, user.getTeamId()))
            throw new ForbiddenException("Task cannot be resubmitted");
        Map<Long, JsonNode> remappedSubmission = remapSubmission(submission);
        List<TaskBlockEntity> questions = blockRepository.findAllByTaskIdAndType(taskId, ContentBlockType.QUESTION);
        List<TaskSubmissionAnswerEntity> answers = checkAnswers(remappedSubmission, questions);
        int result = countPoints(answers, questions);
        TaskSubmissionEntity entity = TaskSubmissionEntity.builder()
                .points(result)
                .teamId(user.getTeamId())
                .taskId(taskId)
                .build();
        TaskSubmissionEntity savedEntity = taskSubmissionRepository.save(entity);
        taskSubmissionAnswerRepository.saveAll(
                answers.stream()
                        .map(answer -> answer.withTaskSubmissionId(savedEntity.getId()))
                        .collect(Collectors.toList()));
        return entityToModel(savedEntity);
    }

    @Transactional
    public void submitCheckpoint(long checkpointId, Map<String, JsonNode> data, AppUser user) {
        if (!accessService.userHasAccessToCheckpoint(checkpointId, user))
            throw new ForbiddenException("User doesn't have access to checkpoint");
        if (!checkpointRepository.existsById(checkpointId))
            throw new NotFoundException("Checkpoint with id " + checkpointId + " not found");

        Optional<CheckpointSubmissionEntity> previousSubmission = checkpointSubmissionRepository.findByCheckpointId(checkpointId);

        boolean isInReview = previousSubmission
                .map(entity -> entity.getStatus() == CheckpointSubmissionStatus.IN_REVIEW)
                .orElse(false);
        if (isInReview)
            throw new ForbiddenException("Cannot resubmit checkpoint when it is in review");

        previousSubmission.ifPresent(entity ->
                checkpointSubmissionAnswerRepository.deleteAllByCheckpointSubmissionId(entity.getId()));

        List<CheckpointSubmissionAnswerEntity> answers = checkpointBlockRepository.findAllByCheckpointIdOrderByIndex(checkpointId)
                .stream()
                .filter(entity -> entity.getType() == ContentBlockType.QUESTION)
                .map(entity -> CheckpointSubmissionAnswerEntity.builder()
                        .checkpointBlockId(entity.getId())
                        .content(data.get(entity.getId().toString()))
                        .build())
                .collect(Collectors.toList());

        CheckpointSubmissionEntity submission = CheckpointSubmissionEntity.builder()
                .id(previousSubmission.map(CheckpointSubmissionEntity::getId).orElse(null))
                .status(CheckpointSubmissionStatus.IN_REVIEW)
                .teamId(user.getTeamId())
                .checkpointId(checkpointId)
                .build();
        CheckpointSubmissionEntity savedEntity = checkpointSubmissionRepository.save(submission);
        checkpointSubmissionAnswerRepository.saveAll(
                answers.stream()
                        .map(answer -> answer.withCheckpointSubmissionId(savedEntity.getId()))
                        .collect(Collectors.toList()));

    }

    private List<TaskSubmissionAnswerEntity> checkAnswers(Map<Long, JsonNode> submission,
                                                          List<TaskBlockEntity> questions) {
        return questions.stream()
                .map(entity -> {
                    JsonNode content = submission.get(entity.getId());
                    boolean isValid = entity.getAnswer()
                            .equals(new JsonNodeAnswerComparator(), content);
                    return TaskSubmissionAnswerEntity.builder()
                            .valid(isValid)
                            .content(content)
                            .taskBlockId(entity.getId())
                            .build();
                }).collect(Collectors.toList());
    }

    private int countPoints(List<TaskSubmissionAnswerEntity> answers, List<TaskBlockEntity> questions) {
        int correct = answers.stream()
                .mapToInt(answer -> answer.isValid() ? 1 : 0)
                .sum();
        int max = questions.size();
        return correct * 10 / max;
    }

    private Map<Long, JsonNode> remapSubmission(Map<String, JsonNode> submission) {
        return submission.entrySet().stream()
                .filter(entry -> {
                    try { Long.parseLong(entry.getKey()); return true; }
                    catch (Exception e) { return false; }
                })
                .map(entry -> Map.entry(Long.parseLong(entry.getKey()), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private TaskSubmissionResult entityToModel(TaskSubmissionEntity entity) {
        return TaskSubmissionResult.builder()
                .id(entity.getId())
                .teamId(entity.getTeamId())
                .content(taskSubmissionAnswerRepository.findAllByTaskSubmissionId(entity.getId())
                        .stream()
                        .collect(Collectors.toMap(
                                TaskSubmissionAnswerEntity::getTaskBlockId,
                                TaskSubmissionAnswerEntity::getContent)))
                .points(entity.getPoints())
                .build();
    }
}
