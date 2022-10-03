package com.javaica.avp.checkpoint;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.checkpoint.model.GradedCheckpoint;
import com.javaica.avp.submission.SubmissionService;
import com.javaica.avp.submission.checkpoint.CheckpointSubmissionResult;
import com.javaica.avp.user.AppUser;
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
@AllArgsConstructor
@RequestMapping("/api/v1/checkpoints")
public class CheckpointController {

    private final CheckpointService checkpointService;
    private final SubmissionService submissionService;

    @GetMapping("/{checkpointId}")
    @Operation(
            summary = "Get checkpoint",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public GradedCheckpoint getStageCheckpoint(
            @PathVariable Long checkpointId,
            @Parameter(hidden = true) @AuthenticationPrincipal AppUser user
    ) {
        return checkpointService.getCheckpointById(checkpointId, user);
    }

    @PostMapping("/{checkpointId}/submissions")
    @Operation(
            summary = "Submit checkpoint solution",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public CheckpointSubmissionResult submitCheckpointSolution(
            @PathVariable Long checkpointId,
            @RequestBody Map<String, JsonNode> submission,
            @Parameter(hidden = true) @AuthenticationPrincipal AppUser user
    ) {
        return submissionService.submitCheckpoint(checkpointId, submission, user);
    }
}
