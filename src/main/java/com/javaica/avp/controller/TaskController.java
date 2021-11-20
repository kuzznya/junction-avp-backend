package com.javaica.avp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Task;
import com.javaica.avp.model.TaskRequest;
import com.javaica.avp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

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
        return taskService.getTaskById(taskId);
    }

    @PostMapping
    @Secured("ROLE_USER")
    @Operation(
            summary = "Create new task",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public Task createNewTask(@Valid @RequestBody TaskRequest taskRequest) {
        return taskService.saveTask(taskRequest);
    }

    @PostMapping("/{taskId}/submission")
    @Operation(
            summary = "Submit task solution",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public void submitTaskSolution(@PathVariable Long taskId,
                                   @RequestBody JsonNode submission,
                                   @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {

    }
}
