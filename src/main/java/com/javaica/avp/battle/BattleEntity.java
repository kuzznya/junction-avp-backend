package com.javaica.avp.battle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("battle")
@Value
@AllArgsConstructor
@Builder
public class BattleEntity {
    @Id
    Long id;
    @With
    BattleStatus status;
    Long initiatorId;
    Long defenderId;
    Long checkpointId;
}
