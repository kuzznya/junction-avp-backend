package com.javaica.avp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.*;
import com.javaica.avp.service.CheckpointService;
import com.javaica.avp.service.SubmissionService;
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
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class CheckpointController {

    private final CheckpointService checkpointService;
    private final SubmissionService submissionService;

    @GetMapping("/checkpoints/{checkpointId}")
    @Operation(
            summary = "Get checkpoint",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public GradedCheckpoint getStageCheckpoint(@PathVariable Long checkpointId,
                                               @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return checkpointService.getCheckpointById(checkpointId, user);
    }

    @PostMapping("/checkpoints")
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Create checkpoint",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Checkpoint createCheckpoint(@Valid @RequestBody CheckpointRequest checkpoint) {
        return checkpointService.createCheckpoint(checkpoint);
    }

    @PostMapping("/checkpoints/{checkpointId}/submissions")
    @Operation(
            summary = "Submit checkpoint solution",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public CheckpointSubmissionResult submitCheckpointSolution(@PathVariable Long checkpointId,
                                                               @RequestBody Map<String, JsonNode> submission,
                                                               @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return submissionService.submitCheckpoint(checkpointId, submission, user);
    }

    @GetMapping("/checkpoints/{checkpointId}/submissions")
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Get all submissions of checkpoint",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public List<CheckpointSubmissionResult> getSubmissions(@PathVariable Long checkpointId) {
        return submissionService.getAllSubmissions(checkpointId);
    }

    @PostMapping("/submissions/{submissionId}/review")
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Get all submissions of checkpoint",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public CheckpointSubmissionResult submitReview(@PathVariable Long submissionId,
                                                   @RequestBody Review review) {
        return submissionService.submitReview(submissionId, review);
    }
}
