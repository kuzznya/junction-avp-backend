package com.javaica.avp.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.javaica.avp.model.CheckpointSubmissionStatus;
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
    CheckpointSubmissionStatus status;
    int points;
}
