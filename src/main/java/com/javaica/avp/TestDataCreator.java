package com.javaica.avp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaica.avp.battle.Battle;
import com.javaica.avp.battle.BattleService;
import com.javaica.avp.checkpoint.CheckpointService;
import com.javaica.avp.checkpoint.model.Checkpoint;
import com.javaica.avp.checkpoint.model.CheckpointBlock;
import com.javaica.avp.checkpoint.model.CheckpointRequest;
import com.javaica.avp.common.ContentBlockType;
import com.javaica.avp.course.Course;
import com.javaica.avp.course.CourseRepository;
import com.javaica.avp.course.CourseService;
import com.javaica.avp.group.Group;
import com.javaica.avp.group.GroupService;
import com.javaica.avp.stage.StageRequest;
import com.javaica.avp.stage.StageService;
import com.javaica.avp.task.TaskService;
import com.javaica.avp.task.model.Task;
import com.javaica.avp.task.model.TaskBlockRequest;
import com.javaica.avp.task.model.TaskRequest;
import com.javaica.avp.team.Team;
import com.javaica.avp.team.TeamService;
import com.javaica.avp.user.AppUser;
import com.javaica.avp.user.UserRole;
import com.javaica.avp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
                .description("Learn to work in a team. " +
                        "In this task you have to connect with your team and discuss the first organizational issues")
                .stageId(stage1.getId())
                .index(0)
                .blocks(List.of(
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.TEXT)
                                .content("the action or process of causing a group of people to work together effectively as a team, " +
                                        "especially by means of activities and events designed to increase motivation and promote cooperation.\n" +
                                        "\"companies are starting to turn to arts-based training programmes as a way of team building and improving morale\"")
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Цhat is the most important thing when participating in a team?")
                                .answer(mapper.readTree("\"cohesion\""))
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Place in correct order: 1 - Go about your business, 2 - Discuss everything with your team")
                                .answer(mapper.readTree("[2, 1]"))
                                .build()
                        )
                ).build();
        Task task1 = taskService.saveTask(taskRequest1);
        log.info("Task {} created", task1);

        TaskRequest taskRequest2 = TaskRequest.builder()
                .name("Task scheduling")
                .description("In this task you have to learn how to distribute responsibilities between the participants")
                .stageId(stage2.getId())
                .index(0)
                .blocks(List.of(
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.TEXT)
                                .content("There are a lot of tools to plan your work - " +
                                        "Trello, Jira, etc. You can choose any tool")
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("What is the most popular task tracker called?")
                                .answer(mapper.readTree("\"jira\""))
                                .build()
                        )
                ).build();
        Task task2 = taskService.saveTask(taskRequest2);
        log.info("Task {} created", task2);

        TaskRequest taskRequest3 = TaskRequest.builder()
                .name("What is MVP?")
                .description("In this task we have to find out what an MVP is and why we need it")
                .stageId(stage3.getId())
                .index(0)
                .blocks(List.of(
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.TEXT)
                                        .content("A minimum viable product, or MVP, is a product with enough features to attract early-adopter customers")
                                        .build(),
                                TaskBlockRequest.builder()
                                        .type(ContentBlockType.QUESTION)
                                        .content("Can MVP attract first customers?")
                                        .answer(mapper.readTree("\"yes\""))
                                        .build()
                        )
                ).build();
        Task task3 = taskService.saveTask(taskRequest3);
        log.info("Task {} created", task3);

        TaskRequest taskRequest4 = TaskRequest.builder()
                .name("What is marketing?")
                .description("In this task we will learn what marketing is and how to use it for our purposes")
                .stageId(stage4.getId())
                .index(0)
                .blocks(List.of(
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.TEXT)
                                .content("Marketing is the action or business of promoting and selling products or services, including market research and advertising.")
                                .build(),
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Can we increase our profit using marketing?")
                                .answer(mapper.readTree("\"yes\""))
                                .build()
                        )
                ).build();
        Task task4 = taskService.saveTask(taskRequest4);
        log.info("Task {} created", task4);

        TaskRequest taskRequest5 = TaskRequest.builder()
                .name("IPO")
                .description("In this task we have to find out what an IPO is")
                .stageId(stage5.getId())
                .index(0)
                .blocks(List.of(
                        TaskBlockRequest.builder()
                                .type(ContentBlockType.TEXT)
                                .content("An IPO is an initial public offering. In an IPO, a privately owned company " +
                                        "lists its shares on a stock exchange, making them available for purchase by " +
                                        "the general public. Many people think of IPOs as big money-making " +
                                        "opportunities—high-profile companies grab headlines with huge share price " +
                                        "gains when they go public.")
                                .build(),
                            TaskBlockRequest.builder()
                                    .type(ContentBlockType.QUESTION)
                                    .content("Is an MVP suitable for entering the IPO?")
                                    .answer(mapper.readTree("\"no\""))
                                    .build()
                        )
                ).build();
        Task task5 = taskService.saveTask(taskRequest5);
        log.info("Task {} created", task5);

        CheckpointRequest checkpointRequest1 = CheckpointRequest.builder()
                .stageId(stage1.getId())
                .name("Team building")
                .description("Submit your discussion results")
                .blocks(List.of(
                        CheckpointBlock.builder()
                                .type(ContentBlockType.TEXT)
                                .content("You should upload result of your team building session")
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Write your answer here")
                                .build()
                )).build();
        Checkpoint checkpoint1 = checkpointService.createCheckpoint(checkpointRequest1);
        log.info("Checkpoint {} created", checkpoint1);

        CheckpointRequest checkpointRequest2 = CheckpointRequest.builder()
                .stageId(stage2.getId())
                .name("Task scheduling")
                .description("Provide results of your task scheduling")
                .blocks(List.of(
                        CheckpointBlock.builder()
                                .type(ContentBlockType.TEXT)
                                .content("You need to upload link to your task tracker like Jira or Trello")
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Upload link here")
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
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Upload your ideas here")
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
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Upload your ideas here")
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
                                .build(),
                        CheckpointBlock.builder()
                                .type(ContentBlockType.QUESTION)
                                .content("Upload link to the document here")
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
