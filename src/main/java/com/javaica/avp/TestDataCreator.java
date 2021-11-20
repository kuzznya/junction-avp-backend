package com.javaica.avp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaica.avp.model.*;
import com.javaica.avp.service.*;
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
    private final StageService stageService;
    private final TaskService taskService;
    private final CheckpointService checkpointService;

    private final ObjectMapper mapper;

    @Override
    public void run(String... args) throws Exception {
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
        AppUser userWithoutCourse = AppUser.builder()
                .username("user_without_course")
                .name("User")
                .surname("Surname")
                .email("user@mail")
                .password("password")
                .build();
        userWithoutCourse = userService.createUser(userWithoutCourse);

        Course course = Course.builder()
                .name("Test course")
                .description("Test description")
                .build();
        course = courseService.createCourse(course);
        log.info("Course {} created", course);
        Group group = Group.builder()
                .courseId(course.getId())
                .complexityLevel(3)
                .build();
        group = groupService.createGroup(group);
        log.info("Group {} created", group);
        Team team = Team.builder()
                .name("Test team 1")
                .members(List.of("admin", "user1", "user2"))
                .groupId(group.getId())
                .build();
        team = teamService.createTeam(team);
        log.info("Team {} created", team);
        Team team2 = Team.builder()
                .name("Test team 2")
                .members(List.of("user3"))
                .groupId(group.getId())
                .build();
        team2 = teamService.createTeam(team2);
        log.info("Team {} created", team2);

        StageRequest stageRequest = StageRequest.builder()
                .name("Stage 1")
                .description("First stage")
                .courseId(course.getId())
                .build();
        var stage = stageService.saveStage(stageRequest);
        log.info("Stage {} created", stage);

        TaskRequest taskRequest = TaskRequest.builder()
                .name("Task 1")
                .description("First task")
                .stageId(stage.getId())
                .index(0)
                .blocks(List.of(
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.TEXT)
                                .content("Ladno. Text.")
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Ty pidor?")
                                .answer(mapper.readTree("\"Da\""))
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Place in correct order: 1 - Get up in the morning, 2 - Drink beer")
                                .answer(mapper.readTree("[1, 2]"))
                                .build()
                        )
                ).build();
        Task task = taskService.saveTask(taskRequest);
        log.info("Task {} created", task);

        Checkpoint checkpoint = Checkpoint.builder()
                .stageId(stage.getId())
                .name("Very important checkpoints")
                .description("Not very important actually")
                .build();
        checkpoint = checkpointService.createCheckpoint(checkpoint);
    }
}
