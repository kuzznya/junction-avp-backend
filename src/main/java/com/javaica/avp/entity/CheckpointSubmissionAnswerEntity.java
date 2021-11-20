package com.javaica.avp.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("checkpoint_submission_answer")
@Value
@AllArgsConstructor
@Builder
public class CheckpointSubmissionAnswerEntity {
    @Id
    Long id;
    @With
    Long checkpointSubmissionId;
    Long checkpointBlockId;
    JsonNode content;
}
