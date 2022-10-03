package com.javaica.avp.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("task")
@Value
@AllArgsConstructor
@Builder
public class TaskEntity {
    @Id
    Long id;
    Long stageId;
    String name;
    String description;
    @With
    Integer index;
}
