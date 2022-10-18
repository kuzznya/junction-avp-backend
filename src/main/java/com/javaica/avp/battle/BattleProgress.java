package com.javaica.avp.battle;

import com.javaica.avp.stage.StageProgress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class BattleProgress {
    Battle battle;
    List<StageProgress> initiatorProgress;
    List<StageProgress> defenderProgress;
    UserTeamRole userTeamRole;
    public enum UserTeamRole {
        INITIATOR,
        DEFENDER
    }
}
