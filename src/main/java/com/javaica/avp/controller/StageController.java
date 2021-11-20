package com.javaica.avp.controller;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Stage;
import com.javaica.avp.model.StageRequest;
import com.javaica.avp.service.StageService;
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
@RequestMapping("/stages")
@AllArgsConstructor
public class StageController {

    private StageService stageService;

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
        return stageService.getStageById(stageId, user);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Create stage",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Stage createStage(@Valid @RequestBody StageRequest stage) {
        return stageService.saveStage(stage);
    }
}
