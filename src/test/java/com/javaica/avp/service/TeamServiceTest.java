package com.javaica.avp.service;

import com.javaica.avp.team.Team;
import com.javaica.avp.team.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Test
    public void addUserToTeamTest() {
        // given
        Optional<Team> team = teamService.getTeam(3);
        Assertions.assertTrue(team.isPresent());
        int countBeforeAdding = team.get().getMembers().size();
        // when
        Team teamAfterAddingUser = teamService.addUserToTeam(3L, "user10");
        //then
        assertEquals(teamAfterAddingUser.getMembers().size(), countBeforeAdding + 1);
    }
}
