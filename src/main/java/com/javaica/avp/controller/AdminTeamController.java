package com.javaica.avp.controller;

import com.javaica.avp.entity.GradedTeamProjection;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.Team;
import com.javaica.avp.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/teams")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminTeamController {

    private final TeamService service;

    @PostMapping
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

    @PostMapping("/{teamId}/users")
    @Operation(
            summary = "Add user to team",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
            })
    public Team addUserToTeam(@PathVariable Long teamId,
                              @RequestParam String username) {
        return service.addUserToTeam(teamId, username);
    }

    @GetMapping
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

    @GetMapping("/{teamId}")
    @Operation(
            summary = "Get team",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Team getTeam(@PathVariable Long teamId) {
        return service.getTeam(teamId).orElseThrow(() -> new NotFoundException("Team not found"));
    }

    @GetMapping("/{teamId}/leaderboard")
    @Operation(
            summary = "Get leaderboard",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public List<GradedTeamProjection> getLeaderboard(@PathVariable Long teamId) {
        return service.getLeaderboard(teamId);
    }

    @DeleteMapping("/{teamId}")
    @Operation(
            summary = "Delete team",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public void deleteTeam(@PathVariable Long teamId) {
        service.deleteTeam(teamId);
    }
}
