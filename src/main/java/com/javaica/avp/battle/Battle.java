package com.javaica.avp.battle;

import com.javaica.avp.team.TeamHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Battle {
    Long id;
    BattleStatus status;
    TeamHeader initiator;
    TeamHeader defender;
    Long checkpointId;
}
