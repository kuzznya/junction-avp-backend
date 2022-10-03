package com.javaica.avp.collab;

import com.javaica.avp.user.AppUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collabs")
@AllArgsConstructor
public class CollabController {

    private CollabService collabService;

    @GetMapping("/current")
    @Operation(
            summary = "Get current collabs",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public List<Collab> getCurrentCollabs(@AuthenticationPrincipal AppUser user) {
        return collabService.getCurrentCollabs(user);
    }

    @GetMapping("/{collabId}")
    @Operation(
            summary = "Get collab by ID",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Collab getCollabById(@PathVariable Long collabId) {
        return collabService.getCollabById(collabId);
    }

    @PostMapping("/initiate")
    @Operation(
            summary = "Initiate collab",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Collab initiateCollab(@RequestParam Long helperId,
                                 @AuthenticationPrincipal AppUser user) {
        return collabService.requestCollab(helperId, user);
    }

    @PostMapping("/{collabId}/accept")
    @Operation(
            summary = "Accept collab request",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Collab acceptCollab(@PathVariable Long collabId,
                               @AuthenticationPrincipal AppUser user) {
        return collabService.acceptCollab(collabId, user);
    }

    @PostMapping("/{collabId}/decline")
    @Operation(
            summary = "Decline collab request",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Collab declineCollab(@PathVariable Long collabId,
                               @AuthenticationPrincipal AppUser user) {
        return collabService.declineCollab(collabId, user);
    }

    @PostMapping("/{collabId}/resolve")
    @Operation(
            summary = "Resolve collab",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public Collab resolveCollab(@PathVariable Long collabId,
                               @AuthenticationPrincipal AppUser user) {
        return collabService.resolveCollab(collabId, user);
    }
}
