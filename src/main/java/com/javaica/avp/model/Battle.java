package com.javaica.avp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Battle {
    BattleStatus status;
    Long initiatorId;
    Long defenderId;
    Long checkpointId;
}
