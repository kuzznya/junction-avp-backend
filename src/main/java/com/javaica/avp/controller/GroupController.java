package com.javaica.avp.controller;

import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Group;
import com.javaica.avp.service.GroupService;
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
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;

    @GetMapping("/current")
    @Operation(
            summary = "Get current group",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Current group not found", content = @Content())
            })
    public Group getCurrentGroup(@Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return service.getCurrentGroup(user).orElseThrow(() -> new NotFoundException("Current group not found"));
    }
}
