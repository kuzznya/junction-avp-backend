package com.javaica.avp.task;

import com.javaica.avp.task.model.Task;
import com.javaica.avp.task.model.TaskRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/tasks")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminTaskController {

    private final TaskService taskService;

    @PostMapping
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

    @DeleteMapping("/{taskId}")
    @Operation(
            summary = "Delete task",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
