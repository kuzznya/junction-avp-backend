package com.javaica.avp.controller;

import com.javaica.avp.model.Course;
import com.javaica.avp.service.CourseService;
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
@RequestMapping("/api/v1/admin/courses")
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService service;

    @PostMapping
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

    @DeleteMapping("/{courseId}")
    @Operation(
            summary = "Delete course",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            })
    public void deleteCourse(@PathVariable Long courseId) {
        service.deleteCourse(courseId);
    }

    @GetMapping
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
}
