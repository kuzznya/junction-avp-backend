package com.javaica.avp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaica.avp.model.*;
import com.javaica.avp.repository.*;
import com.javaica.avp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final BattleService battleService;

    private final CourseRepository courseRepository;

    private final ObjectMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        courseRepository.deleteAll();

        Map<String, AppUser> users = new HashMap<>();

        try {
            AppUser user = AppUser.builder()
                    .username("admin2")
                    .name("Admin")
                    .surname("Another")
                    .email("admin@email")
                    .role(UserRole.ADMIN)
                    .build();
            userService.createUser(user);
        } catch (Exception ignored) {}

        for (int i = 1; i <= 10; i++) {
            try {
                AppUser user = AppUser.builder()
                        .username("user" + i)
                        .name("User" + i)
                        .surname("Surname" + i)
                        .email("user" + i + "@mail")
                        .password("password")
                        .build();
                user = userService.createUser(user);
                log.info("User {} created", user);
            } catch (Exception ignored) {}
        }

        Course course = Course.builder()
                .name("Test course")
                .description("Test description")
                .build();
        course = courseService.createCourse(course);
        log.info("Course {} created", course);
        Group group1 = Group.builder()
                .courseId(course.getId())
                .complexityLevel(3)
                .build();
        group1 = groupService.createGroup(group1);
        log.info("Group {} created", group1);
        Team team = Team.builder()
                .name("Test team 1")
                .members(List.of("admin", "user1", "user2"))
                .groupId(group1.getId())
                .build();
        team = teamService.createTeam(team);
        log.info("Team {} created", team);
        Team team2 = Team.builder()
                .name("Test team 2")
                .members(List.of("user3"))
                .groupId(group1.getId())
                .build();
        team2 = teamService.createTeam(team2);
        log.info("Team {} created", team2);

        Team team3 = Team.builder()
                .name("Test team 3")
                .members(List.of("user4", "user5", "user6"))
                .groupId(group1.getId())
                .build();
        team3 = teamService.createTeam(team3);
        log.info("Team {} created", team3);

        Team team4 = Team.builder()
                .name("Test team 4")
                .members(List.of("user7", "user8"))
                .groupId(group1.getId())
                .build();
        team4 = teamService.createTeam(team4);
        log.info("Team {} created", team4);

        Team team5 = Team.builder()
                .name("Test team 5")
                .members(List.of("user9", "user10"))
                .groupId(group1.getId())
                .build();
        team5 = teamService.createTeam(team5);
        log.info("Team {} created", team5);

        userService.getAllUsers().forEach(user -> users.put(user.getUsername(), user));

        StageRequest stageRequest1 = StageRequest.builder()
                .name("Stage 1")
                .description("First stage")
                .courseId(course.getId())
                .build();
        var stage1 = stageService.saveStage(stageRequest1);
        log.info("Stage {} created", stage1);

        StageRequest stageRequest2 = StageRequest.builder()
                .name("Stage 2")
                .description("Second stage")
                .courseId(course.getId())
                .build();
        var stage2 = stageService.saveStage(stageRequest2);
        log.info("Stage {} created", stage2);

        StageRequest stageRequest3 = StageRequest.builder()
                .name("Stage 3")
                .description("Third stage")
                .courseId(course.getId())
                .build();
        var stage3 = stageService.saveStage(stageRequest3);
        log.info("Stage {} created", stage3);

        StageRequest stageRequest4 = StageRequest.builder()
                .name("Stage 4")
                .description("Fourth stage")
                .courseId(course.getId())
                .build();
        var stage4 = stageService.saveStage(stageRequest4);
        log.info("Stage {} created", stage4);

        StageRequest stageRequest5 = StageRequest.builder()
                .name("Stage 5")
                .description("Fifth stage")
                .courseId(course.getId())
                .build();
        var stage5 = stageService.saveStage(stageRequest5);
        log.info("Stage {} created", stage5);

        TaskRequest taskRequest1 = TaskRequest.builder()
                .name("Team building")
                .description("Learn to work in a team")
                .stageId(stage1.getId())
                .index(0)
                .blocks(List.of(
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.TEXT)
                                .content("Block 1")
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Block 2")
                                .answer(mapper.readTree("\"2\""))
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Place in correct order: 1 - Get up in the morning, 2 - Have fun")
                                .answer(mapper.readTree("[1, 2]"))
                                .build()
                        )
                ).build();
        Task task1 = taskService.saveTask(taskRequest1);
        log.info("Task {} created", task1);

        TaskRequest taskRequest2 = TaskRequest.builder()
                .name("Planning")
                .description("Assign responsibilities")
                .stageId(stage2.getId())
                .index(0)
                .blocks(List.of(
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.QUESTION)
                                        .content("Will you work in a team?")
                                        .answer(mapper.readTree("\"yes\""))
                                        .build()
                        )
                ).build();
        Task task2 = taskService.saveTask(taskRequest2);
        log.info("Task {} created", task2);

        TaskRequest taskRequest3 = TaskRequest.builder()
                .name("Task 3")
                .description("Third task")
                .stageId(stage3.getId())
                .index(0)
                .blocks(List.of(
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.TEXT)
                                        .content("Block 1")
                                        .build(),
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.QUESTION)
                                        .content("Block 2")
                                        .answer(mapper.readTree("\"2\""))
                                        .build(),
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.QUESTION)
                                        .content("Place in correct order: 1 - Get up in the morning, 2 - Do nothing")
                                        .answer(mapper.readTree("[1, 2]"))
                                        .build()
                        )
                ).build();
        Task task3 = taskService.saveTask(taskRequest3);
        log.info("Task {} created", task3);

        TaskRequest taskRequest4 = TaskRequest.builder()
                .name("Have fun!")
                .description("Do nothing. It's time to make a break")
                .stageId(stage4.getId())
                .index(0)
                .blocks(List.of(
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.QUESTION)
                                        .content("Do you have fun?")
                                        .answer(mapper.readTree("\"yes\""))
                                        .build()
                        )
                ).build();
        Task task4 = taskService.saveTask(taskRequest4);
        log.info("Task {} created", task4);

        TaskRequest taskRequest5 = TaskRequest.builder()
                .name("Final task")
                .description("Just another task")
                .stageId(stage5.getId())
                .index(0)
                .blocks(List.of(
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.QUESTION)
                                        .content("Are you ready?")
                                        .answer(mapper.readTree("\"yes\""))
                                        .build()
                        )
                ).build();
        Task task5 = taskService.saveTask(taskRequest5);
        log.info("Task {} created", task5);

        CheckpointRequest checkpointRequest1 = CheckpointRequest.builder()
                .stageId(stage1.getId())
                .name("Very important checkpoints")
                .description("Not very important actually")
                .blocks(List.of(
                        CheckpointBlock.builder()
                                .type(ContentBlockType.TEXT)
                                .content("Here you can find the most important task in this stage. Keep calm and answer.")
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Da?")
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Net?")
                                .build()
                )).build();
        Checkpoint checkpoint1 = checkpointService.createCheckpoint(checkpointRequest1);
        log.info("Checkpoint {} created", checkpoint1);

        CheckpointRequest checkpointRequest2 = CheckpointRequest.builder()
                .stageId(stage2.getId())
                .name("Very-very important checkpoints")
                .description("This checkpoint is actually important")
                .blocks(List.of(
                        CheckpointBlock.builder()
                                .type(ContentBlockType.TEXT)
                                .content("How?")
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Da?")
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Net?")
                                .build()
                )).build();
        Checkpoint checkpoint2 = checkpointService.createCheckpoint(checkpointRequest2);
        log.info("Checkpoint {} created", checkpoint2);

        CheckpointRequest checkpointRequest3 = CheckpointRequest.builder()
                .stageId(stage3.getId())
                .name("It's time to develop MVP!")
                .description("This checkpoint is the most important")
                .blocks(List.of(
                        CheckpointBlock.builder()
                                .type(ContentBlockType.TEXT)
                                .content("Describe your ideas")
                                .build()
                )).build();
        Checkpoint checkpoint3 = checkpointService.createCheckpoint(checkpointRequest3);
        log.info("Checkpoint {} created", checkpoint3);

        CheckpointRequest checkpointRequest4 = CheckpointRequest.builder()
                .stageId(stage4.getId())
                .name("Marketing")
                .description("Provide your marketing strategy")
                .blocks(List.of(
                        CheckpointBlock.builder()
                                .type(ContentBlockType.TEXT)
                                .content("Describe your ideas")
                                .build()
                )).build();
        Checkpoint checkpoint4 = checkpointService.createCheckpoint(checkpointRequest4);
        log.info("Checkpoint {} created", checkpoint4);

        CheckpointRequest checkpointRequest5 = CheckpointRequest.builder()
                .stageId(stage5.getId())
                .name("IPO")
                .description("It's time to go to the IPO!")
                .blocks(List.of(
                        CheckpointBlock.builder()
                                .type(ContentBlockType.TEXT)
                                .content("Provide your strategy")
                                .build()
                )).build();
        Checkpoint checkpoint5 = checkpointService.createCheckpoint(checkpointRequest5);
        log.info("Checkpoint {} created", checkpoint5);

        Battle battle1 = battleService.initiateBattle(
                team.getId(),
                checkpoint3.getId(),
                users.get(team2.getMembers().get(0))
        );

        log.info("Battle {} created", battle1);

        battleService.acceptBattle(battle1.getId(), users.get(team.getMembers().get(0)));

        Battle battle2 = battleService.initiateBattle(
                team.getId(),
                checkpoint3.getId(),
                users.get(team3.getMembers().get(0))
        );

        log.info("Battle {} created", battle2);
    }
}
