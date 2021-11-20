package com.javaica.avp.controller;

import com.javaica.avp.entity.GradedTeamProjection;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Team;
import com.javaica.avp.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;

    @GetMapping("/current")
    @Operation(
            summary = "Get current team",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Current team not found", content = @Content())
            })
    public GradedTeamProjection getCurrentTeam(@Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return service.getTeamOfUser(user)
                .orElseThrow(() -> new NotFoundException("Current team not found"));
    }

    @GetMapping("/leaderboard")
    @Operation(
            summary = "Get all teams",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public List<GradedTeamProjection> getLeaderboard(@Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return service.getLeaderboard(user);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Create team",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
            })
    public Team createTeam(@RequestBody @Valid Team team) {
        return service.createTeam(team);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Get all teams",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
            })
    public List<Team> getAllTeams(@RequestParam("group_id") long groupId) {
        return service.getAllTeams(groupId);
    }
}
