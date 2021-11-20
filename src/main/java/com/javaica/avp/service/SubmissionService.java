package com.javaica.avp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.entity.*;
import com.javaica.avp.exception.BadRequestException;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.*;
import com.javaica.avp.util.JsonNodeAnswerComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
                .submissionTimestamp(Instant.now())
                .build();
        TaskSubmissionEntity savedEntity = taskSubmissionRepository.save(entity);
        taskSubmissionAnswerRepository.saveAll(
                answers.stream()
                        .map(answer -> answer.withTaskSubmissionId(savedEntity.getId()))
                        .collect(Collectors.toList()));
        return taskEntityToModel(savedEntity);
    }

    @Transactional
    public CheckpointSubmissionResult submitCheckpoint(long checkpointId, Map<String, JsonNode> data, AppUser user) {
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

        previousSubmission.ifPresent(checkpointSubmissionRepository::delete);

        List<CheckpointSubmissionAnswerEntity> answers = createAnswersForCheckpoint(data, checkpointId);

        CheckpointSubmissionEntity submission = CheckpointSubmissionEntity.builder()
                .status(CheckpointSubmissionStatus.IN_REVIEW)
                .teamId(user.getTeamId())
                .checkpointId(checkpointId)
                .submissionTimestamp(Instant.now())
                .build();
        CheckpointSubmissionEntity savedEntity = checkpointSubmissionRepository.save(submission);
        checkpointSubmissionAnswerRepository.saveAll(
                answers.stream()
                        .map(answer -> answer.withCheckpointSubmissionId(savedEntity.getId()))
                        .collect(Collectors.toList()));

        return checkpointEntityToModel(savedEntity);
    }

    public List<CheckpointSubmissionResult> getAllSubmissions(long checkpointId) {
        return checkpointSubmissionRepository.findByCheckpointId(checkpointId).stream()
                .map(this::checkpointEntityToModel)
                .collect(Collectors.toList());
    }

    public CheckpointSubmissionResult submitReview(long submissionId, Review review) {
        CheckpointSubmissionEntity submission = checkpointSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new NotFoundException("Submission with id " + submissionId + " not found"));
        if (review.getStatus() != CheckpointSubmissionStatus.ACCEPTED && review.getStatus() != CheckpointSubmissionStatus.DECLINED)
            throw new BadRequestException("Invalid review status");
        if (review.getStatus() == CheckpointSubmissionStatus.ACCEPTED && review.getPoints() == null)
            throw new BadRequestException("Cannot accept submission without points");
        return checkpointEntityToModel(checkpointSubmissionRepository.save(submission.withReview(review.getReview())
                .withStatus(review.getStatus())
                .withPoints(review.getPoints())));
    }

    public Integer getTaskPoints(long taskId) {
        return taskSubmissionRepository.findByTaskId(taskId)
                .map(TaskSubmissionEntity::getPoints)
                .orElse(null);
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

    private List<CheckpointSubmissionAnswerEntity> createAnswersForCheckpoint(Map<String, JsonNode> data, long checkpointId) {
        return checkpointBlockRepository.findAllByCheckpointIdAndType(checkpointId, ContentBlockType.QUESTION)
                .stream()
                .map(entity -> CheckpointSubmissionAnswerEntity.builder()
                        .checkpointBlockId(entity.getId())
                        .content(data.get(entity.getId().toString()))
                        .build())
                .collect(Collectors.toList());
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

    private TaskSubmissionResult taskEntityToModel(TaskSubmissionEntity entity) {
        return TaskSubmissionResult.builder()
                .id(entity.getId())
                .teamId(entity.getTeamId())
                .content(taskSubmissionAnswerRepository.findAllByTaskSubmissionId(entity.getId())
                        .stream()
                        .filter(answer -> answer.getContent() != null)
                        .collect(Collectors.toMap(
                                TaskSubmissionAnswerEntity::getTaskBlockId,
                                TaskSubmissionAnswerEntity::getContent)))
                .points(entity.getPoints())
                .submissionTimestamp(entity.getSubmissionTimestamp())
                .build();
    }

    private CheckpointSubmissionResult checkpointEntityToModel(CheckpointSubmissionEntity entity) {
        return CheckpointSubmissionResult.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .review(entity.getReview())
                .teamId(entity.getTeamId())
                .points(entity.getPoints())
                .content(checkpointSubmissionAnswerRepository.findAllByCheckpointSubmissionId(entity.getId())
                        .stream()
                        .filter(answer -> answer.getContent() != null)
                        .collect(Collectors.toMap(
                                CheckpointSubmissionAnswerEntity::getCheckpointBlockId,
                                CheckpointSubmissionAnswerEntity::getContent)))
                .submissionTimestamp(entity.getSubmissionTimestamp())
                .build();
    }
}
