package com.javaica.avp.checkpoint.model;

import com.javaica.avp.submission.checkpoint.CheckpointSubmissionStatus;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class GradedCheckpointHeader extends CheckpointHeader {

    CheckpointSubmissionStatus status;
    Integer points;

    public GradedCheckpointHeader(Long id, String name, String description, CheckpointSubmissionStatus status, Integer points) {
        super(id, name, description);
        this.status = status;
        this.points = points;
    }
}
