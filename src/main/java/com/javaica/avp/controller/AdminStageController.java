package com.javaica.avp.controller;

import com.javaica.avp.model.Stage;
import com.javaica.avp.model.StageRequest;
import com.javaica.avp.service.StageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/stages")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminStageController {

    private final StageService stageService;

    @PostMapping
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
