package com.javaica.avp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

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
        return null;
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
