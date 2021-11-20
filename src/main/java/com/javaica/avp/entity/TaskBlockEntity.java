package com.javaica.avp.entity;

import com.javaica.avp.model.TaskBlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("task_block")
@Value
@AllArgsConstructor
@Builder
public class TaskBlockEntity {
    @Id
    Long id;
    Long taskId;
    String content;
    TaskBlockType type;
    String answer;
    @With
    Integer index;
}
