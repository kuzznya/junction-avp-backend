package com.javaica.avp.controller;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Course;
import com.javaica.avp.model.StageHeader;
import com.javaica.avp.service.CourseService;
import com.javaica.avp.service.StageService;
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
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/courses")
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
        return null;
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Create new course",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
            })
    public Course createCourse(@RequestBody @Valid Course course) {
        return service.createCourse(course);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    @Operation(
            summary = "Get all courses",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
            })
    public List<Course> getAllCourses() {
        return service.getAllCourses();
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
    public List<StageHeader> getCurrentStages(@PathVariable Long courseId,
                                              @Parameter(hidden = true) @AuthenticationPrincipal AppUser user) {
        return stageService.getStageHeaders(courseId);
    }
}
