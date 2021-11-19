package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("task")
@Value
@AllArgsConstructor
public class TaskEntity {
    @Id
    Long id;
    Long stageId;
    String name;
    String description;
}
