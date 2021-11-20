package com.javaica.avp.model;

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
}
