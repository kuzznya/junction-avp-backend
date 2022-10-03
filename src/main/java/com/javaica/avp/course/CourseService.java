package com.javaica.avp.course;

import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.group.GroupService;
import com.javaica.avp.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;
    private final GroupService groupService;

    public Course getCurrentCourse(AppUser user) {
        return entityToModel(
                repository.findById(
                        groupService
                                .getCurrentGroup(user)
                                .orElseThrow(() -> new NotFoundException("User does not belong to any group"))
                                .getCourseId())
                    .orElseThrow(() -> new NotFoundException("User does not belong to any course"))
        );
    }

    public Course getTeamCourse(Long teamId) {
        return entityToModel(
                repository.findCourseByTeamId(teamId)
                        .orElseThrow(() -> new NotFoundException("Course for team " + teamId + " not found"))
        );
    }

    public Course createCourse(Course course) {
        CourseEntity entity = modelToEntity(course.withId(null));
        return entityToModel(repository.save(entity));
    }

    public void deleteCourse(long courseId) {
        repository.deleteById(courseId);
    }

    public List<Course> getAllCourses() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    private Course entityToModel(CourseEntity entity) {
        return Course.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    private CourseEntity modelToEntity(Course course) {
        return CourseEntity.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .build();
    }
}
