package com.javaica.avp.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.TaskSubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("task_submission")
@Value
@AllArgsConstructor
public class TaskSubmissionEntity {
    @Id
    Long id;
    Long teamId;
    JsonNode content;
    int points;
}
