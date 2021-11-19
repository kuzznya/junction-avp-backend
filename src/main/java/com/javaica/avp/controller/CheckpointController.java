package com.javaica.avp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Checkpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkpoints")
public class CheckpointController {

    @GetMapping("/{checkpointId}")
    public Checkpoint getStageCheckpoint(@PathVariable Long checkpointId,
                                         @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return null;
    }

    @PostMapping("/{checkpointId}/submission")
    @Operation(
            summary = "Submit checkpoint solution",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public void submitCheckpointSolution(@PathVariable Long checkpointId,
                                         @RequestBody JsonNode submission,
                                         @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {

    }
}
