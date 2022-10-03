package com.javaica.avp.checkpoint.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.javaica.avp.submission.checkpoint.CheckpointSubmissionStatus;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GradedCheckpointProjection extends CheckpointEntity {

    CheckpointSubmissionStatus status;
    Integer points;

    public GradedCheckpointProjection(Long id, Long stageId, String name, String description, CheckpointSubmissionStatus status, Integer points) {
        super(id, stageId, name, description);
        this.status = status;
        this.points = points;
    }
}
