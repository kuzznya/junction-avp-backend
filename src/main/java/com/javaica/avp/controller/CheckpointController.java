package com.javaica.avp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Checkpoint;
import com.javaica.avp.service.CheckpointService;
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
@RequestMapping("/checkpoints")
@AllArgsConstructor
public class CheckpointController {

    private final CheckpointService checkpointService;

    @GetMapping("/{checkpointId}")
    public Checkpoint getStageCheckpoint(@PathVariable Long checkpointId,
                                         @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return checkpointService.getCheckpointById(checkpointId);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public Checkpoint createCheckpoint(@Valid @RequestBody Checkpoint checkpoint) {
        return checkpointService.createCheckpoint(checkpoint);
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
