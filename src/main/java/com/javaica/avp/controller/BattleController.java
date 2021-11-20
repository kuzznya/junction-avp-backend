package com.javaica.avp.controller;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Battle;
import com.javaica.avp.service.BattleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/battles")
@AllArgsConstructor
public class BattleController {

    private final BattleService battleService;

    @GetMapping("/current")
    @Operation(
            summary = "Get user battles",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public List<Battle> getUserBattles(@AuthenticationPrincipal AppUser user) {
        return battleService.getUserBattles(user);
    }

    @GetMapping("/{battleId}")
    @Operation(
            summary = "Get battle",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Battle getBattleById(@PathVariable Long battleId,
                                @AuthenticationPrincipal AppUser user) {
        return battleService.getBattleById(battleId, user);
    }

    @PostMapping("/initiate")
    @Operation(
            summary = "Initiate battle",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Battle initiateBattle(@RequestParam Long opponentId,
                                 @RequestParam Long checkpointId,
                                 @AuthenticationPrincipal AppUser user) {
        return battleService.initiateBattle(opponentId, checkpointId, user);
    }

    @PostMapping("/{battleId}/accept")
    @Operation(
            summary = "Accept battle",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Battle acceptBattle(@PathVariable Long battleId,
                               @AuthenticationPrincipal AppUser user) {
        return battleService.acceptBattle(battleId, user);
    }

    @PostMapping("/{battleId}/decline")
    @Operation(
            summary = "Decline battle",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Battle declineBattle(@PathVariable Long battleId,
                               @AuthenticationPrincipal AppUser user) {
        return battleService.declineBattle(battleId, user);
    }
}
