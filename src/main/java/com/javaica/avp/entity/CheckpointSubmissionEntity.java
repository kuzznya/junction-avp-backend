package com.javaica.avp.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.TaskSubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("checkpoint_submission")
@Value
@AllArgsConstructor
public class CheckpointSubmissionEntity {
    @Id
    Long id;
    Long teamId;
    Long checkpointId;
    JsonNode content;
    TaskSubmissionStatus status;
    int points;
}
