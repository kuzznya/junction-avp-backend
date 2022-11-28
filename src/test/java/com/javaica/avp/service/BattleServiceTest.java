package com.javaica.avp.service;

import com.javaica.avp.battle.BattleProgress;
import com.javaica.avp.battle.BattleService;
import com.javaica.avp.user.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class BattleServiceTest {

    @Autowired
    private BattleService battleService;

    @Test
    public void getBattleProgressTest() {
        BattleProgress battleProgress = battleService.getBattleProgress(2L, AppUser.builder().teamId(3L).build());

        assertEquals(battleProgress.getBattle().getDefender().getName(), "Test team 1");
        assertEquals(battleProgress.getBattle().getInitiator().getName(), "Test team 3");
    }
}
