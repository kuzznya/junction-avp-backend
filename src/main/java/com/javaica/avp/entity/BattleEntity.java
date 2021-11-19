package com.javaica.avp.entity;

import com.javaica.avp.model.BattleStatus;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("battle")
@Value
@AllArgsConstructor
public class BattleEntity {
    @Id
    Long id;
    BattleStatus status;
    Long initiatorId;
    Long defenderId;
}
