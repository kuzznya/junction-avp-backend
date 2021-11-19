package com.javaica.avp;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Course;
import com.javaica.avp.model.Group;
import com.javaica.avp.model.Team;
import com.javaica.avp.service.CourseService;
import com.javaica.avp.service.GroupService;
import com.javaica.avp.service.TeamService;
import com.javaica.avp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class TestDataCreator implements CommandLineRunner {

    private final UserService userService;
    private final CourseService courseService;
    private final GroupService groupService;
    private final TeamService teamService;

    @Override
    public void run(String... args) {
        for (int i = 1; i <= 3; i++) {
            AppUser user = AppUser.builder()
                    .username("user" + i)
                    .name("User" + i)
                    .surname("Surname" + i)
                    .email("user" + i + "@mail")
                    .password("password")
                    .build();
            user = userService.createUser(user);
            log.info("User {} created", user);
        }
        Course course = Course.builder()
                .name("Test course")
                .description("Test description")
                .build();
        course = courseService.createCourse(course);
        log.info("Course {} created", course);
        Group group = Group.builder()
                .complexityLevel(3)
                .build();
        group = groupService.createGroup(course.getId(), group);
        log.info("Group {} created", group);
        Team team = Team.builder()
                .name("Test team 1")
                .members(List.of("admin", "user1", "user2"))
                .build();
        team = teamService.createTeam(group.getId(), team);
        log.info("Team {} created", team);
        Team team2 = Team.builder()
                .name("Test team 2")
                .members(List.of("user3"))
                .build();
        team2 = teamService.createTeam(group.getId(), team2);
        log.info("Team {} created", team2);
    }
}
