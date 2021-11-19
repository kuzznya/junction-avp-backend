package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("stage")
@Value
@AllArgsConstructor
@Builder
public class StageEntity {
    @Id
    Long id;
    Long courseId;
    String name;
    String description;
}
