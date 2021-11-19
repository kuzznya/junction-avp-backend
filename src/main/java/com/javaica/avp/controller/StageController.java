package com.javaica.avp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/stages")
public class StageController {

    @GetMapping
    @Operation(
            summary = "Get stages for current course",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public List<StageHeader> getCurrentStages(@Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return Collections.emptyList();
    }

    @GetMapping("/{stageId}")
    @Operation(
            summary = "Get stage by ID",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Stage getStageById(@PathVariable Long stageId,
                              @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return null;
    }
}
