package com.javaica.avp.controller;

import com.javaica.avp.model.Group;
import com.javaica.avp.service.GroupService;
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
@RequestMapping("/api/v1/admin/groups")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminGroupController {

    private final GroupService service;

    @PostMapping
    @Operation(
            summary = "Create new group",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
            })
    public Group createNewGroup(@RequestBody @Valid Group group) {
        return service.createGroup(group);
    }

    @GetMapping
    @Operation(
            summary = "Get all groups",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
            })
    public List<Group> getAllGroups(@RequestParam("course_id") long courseId) {
        return service.getAllGroups(courseId);
    }

    @DeleteMapping("/{groupId}")
    @Operation(
            summary = "Get all groups",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public void deleteGroup(@PathVariable Long groupId) {
        service.deleteGroup(groupId);
    }
}
