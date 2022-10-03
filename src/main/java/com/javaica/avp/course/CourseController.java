package com.javaica.avp.course;

import com.javaica.avp.stage.StageHeader;
import com.javaica.avp.stage.StageService;
import com.javaica.avp.user.AppUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;
    private final StageService stageService;

    @GetMapping("/current")
    @Operation(
            summary = "Get current course",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Current course not found", content = @Content())
            })
    public Course getCurrentCourse(@Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return service.getCurrentCourse(user);
    }

    @GetMapping("/current/stages")
    @Operation(
            summary = "Get stages for current course",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public List<StageHeader> getCurrentStages(@Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return stageService.getCurrentStageHeaders(user);
    }

    @GetMapping("/{courseId}/stages")
    @Operation(
            summary = "Get stages for current course",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            })
    public List<StageHeader> getStages(@PathVariable Long courseId,
                                       @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return stageService.getStageHeaders(courseId, user);
    }
}
