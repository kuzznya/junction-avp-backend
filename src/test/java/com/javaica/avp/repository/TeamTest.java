package com.javaica.avp.repository;

import com.javaica.avp.team.GradedTeamProjection;
import com.javaica.avp.team.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TeamTest {

    @Autowired
    private TeamRepository repository;

    @Test
    public void getLeaderboardTest() {
        List<GradedTeamProjection> teamProjectionList = repository.getLeaderboard(1);

        assertEquals(teamProjectionList.size(), 5);
    }
}
