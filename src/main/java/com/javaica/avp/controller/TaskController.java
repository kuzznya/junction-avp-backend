package com.javaica.avp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Task;
import com.javaica.avp.model.TaskSubmissionResult;
import com.javaica.avp.service.SubmissionService;
import com.javaica.avp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final SubmissionService submissionService;

    @GetMapping("/{taskId}")
    @Operation(
            summary = "Get task by ID",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Task getStageTaskById(@PathVariable Long taskId,
                                 @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return taskService.getTaskById(taskId, user);
    }

    @PostMapping("/{taskId}/submissions")
    @Operation(
            summary = "Submit task solution",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public TaskSubmissionResult submitTaskSolution(@PathVariable long taskId,
                                                   @RequestBody Map<String, JsonNode> submission,
                                                   @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return submissionService.submitTask(taskId, submission, user);
    }
}
