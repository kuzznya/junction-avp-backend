package com.javaica.avp.battle;

import com.javaica.avp.user.AppUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/battles")
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
    public Battle getUserBattle(@AuthenticationPrincipal AppUser user) {
        return battleService.getUserBattle(user);
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

    @GetMapping("/{battleId}/progress")
    public BattleProgress getBattleProgress(@PathVariable Long battleId,
                                            @AuthenticationPrincipal AppUser user) {
        return battleService.getBattleProgress(battleId, user);
    }

    @GetMapping("/current/progress")
    public BattleProgress getCurrentBattleProgress(@AuthenticationPrincipal AppUser user) {
        return battleService.getCurrentBattleProgress(user);
    }
}
