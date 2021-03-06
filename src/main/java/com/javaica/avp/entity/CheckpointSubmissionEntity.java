package com.javaica.avp.entity;

import com.javaica.avp.model.CheckpointSubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("checkpoint_submission")
@Value
@AllArgsConstructor
@Builder
public class CheckpointSubmissionEntity {
    @Id
    Long id;
    Long teamId;
    Long checkpointId;
    @With
    CheckpointSubmissionStatus status;
    @With
    Integer points;
    @With
    String review;
    Instant submissionTimestamp;
}
