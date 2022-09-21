package com.javaica.avp.controller;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Get all users",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }
}
