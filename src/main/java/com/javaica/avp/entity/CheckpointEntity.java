package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("checkpoint")
@Value
@NonFinal
@AllArgsConstructor
@Builder
public class CheckpointEntity {
    @Id
    Long id;
    Long stageId;
    String name;
    String description;
}
