package com.javaica.avp.controller;

import com.javaica.avp.model.Checkpoint;
import com.javaica.avp.model.CheckpointRequest;
import com.javaica.avp.model.CheckpointSubmissionResult;
import com.javaica.avp.model.Review;
import com.javaica.avp.service.CheckpointService;
import com.javaica.avp.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminCheckpointController {

    private final CheckpointService checkpointService;
    private final SubmissionService submissionService;

    @PostMapping("/checkpoints")
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

    @GetMapping("/checkpoints/{checkpointId}/submissions")
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
