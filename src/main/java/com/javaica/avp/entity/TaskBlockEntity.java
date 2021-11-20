package com.javaica.avp.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.ContentBlockType;
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
    @With
    Long id;
    Long taskId;
    String content;
    ContentBlockType type;
    JsonNode answer;
    @With
    Integer index;
}
