package com.javaica.avp.controller;

import com.javaica.avp.model.Battle;
import com.javaica.avp.service.BattleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/battles")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminBattleController {

    private final BattleService battleService;

    @GetMapping
    @Operation(
            summary = "Get all battles",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public List<Battle> getAllBattles() {
        return battleService.getAllBattles();
    }

    @GetMapping("/{teamId}")
    @Operation(
            summary = "Get team battles",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Battle getTeamBattles(@PathVariable Long teamId) {
        return battleService.getTeamBattles(teamId);
    }
}
