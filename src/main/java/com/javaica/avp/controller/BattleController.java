package com.javaica.avp.controller;

import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.Battle;
import com.javaica.avp.service.BattleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/battles")
@AllArgsConstructor
public class BattleController {

    private final BattleService battleService;

    @GetMapping("/current")
    public List<Battle> getUserBattles(@AuthenticationPrincipal AppUser user) {
        return battleService.getUserBattles(user);
    }

    @GetMapping("/{battleId}")
    public Battle getBattleById(@PathVariable Long battleId,
                                @AuthenticationPrincipal AppUser user) {
        return battleService.getBattleById(battleId, user);
    }

    @PostMapping("/initiate")
    public Battle initiateBattle(@RequestParam Long opponentId,
                                 @AuthenticationPrincipal AppUser user) {
        return battleService.initiateBattle(opponentId, user);
    }

    @PostMapping("/{battleId}/accept")
    public Battle acceptBattle(@PathVariable Long battleId,
                               @AuthenticationPrincipal AppUser user) {
        return battleService.acceptBattle(battleId, user);
    }

    @PostMapping("/{battleId}/decline")
    public Battle declineBattle(@PathVariable Long battleId,
                               @AuthenticationPrincipal AppUser user) {
        return battleService.declineBattle(battleId, user);
    }
}
