package com.javaica.avp.entity;

import com.javaica.avp.model.BattleStatus;
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
}
