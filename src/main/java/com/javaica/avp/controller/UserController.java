package com.javaica.avp.controller;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create user", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or occupied username", content = @Content())
    })
    public AppUser createUser(@Valid @RequestBody AppUser user) {
        return userService.createUser(user);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public AppUser getCurrentUser(@Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return user;
    }
}
