package com.javaica.avp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.javaica.avp.entity.TaskBlockEntity;
import com.javaica.avp.entity.TaskSubmissionEntity;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.TaskBlockType;
import com.javaica.avp.model.TaskSubmissionResult;
import com.javaica.avp.repository.TaskBlockRepository;
import com.javaica.avp.repository.TaskRepository;
import com.javaica.avp.repository.TaskSubmissionRepository;
import com.javaica.avp.util.JsonNodeAnswerComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

    private final AccessService accessService;
    private final TaskRepository taskRepository;
    private final TaskBlockRepository blockRepository;
    private final TaskSubmissionRepository taskSubmissionRepository;
    private final ObjectMapper objectMapper;

    public TaskSubmissionResult submitTask(long taskId, Map<String, JsonNode> submission, AppUser user) {
        if (!accessService.userHasAccessToTask(taskId, user))
            throw new ForbiddenException("User doesn't have access to the task");
        if (!taskRepository.existsById(taskId))
            throw new NotFoundException("Task with id " + taskId + " not found");
        if (taskSubmissionRepository.existsByTaskIdAndTeamId(taskId, user.getTeamId()))
            throw new ForbiddenException("Task cannot be resubmitted");
        Map<Long, JsonNode> remappedSubmission = remapSubmission(submission);
        int result = countPoints(taskId, remappedSubmission);
        ObjectNode contentNode = objectMapper.createObjectNode();
        remappedSubmission.forEach((key, value) -> contentNode.set(key.toString(), value));
        TaskSubmissionEntity entity = TaskSubmissionEntity.builder()
                .content(contentNode)
                .points(result)
                .teamId(user.getTeamId())
                .taskId(taskId)
                .build();
        return entityToModel(taskSubmissionRepository.save(entity));
    }

    private int countPoints(long taskId, Map<Long, JsonNode> submission) {
        List<TaskBlockEntity> questions = blockRepository.findAllByTaskIdAndType(taskId, TaskBlockType.QUESTION);
        int correct = questions.stream()
                .peek(entity -> log.debug("Comparing {} and {}", entity.getAnswer(), submission.get(entity.getId())))
                .map(entity -> entity.getAnswer().equals(new JsonNodeAnswerComparator(), submission.get(entity.getId())))
                .mapToInt(isCorrect -> isCorrect ? 1 : 0)
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
                .content(entity.getContent())
                .points(entity.getPoints())
                .build();
    }
}
