package com.javaica.avp.checkpoint.model;

import com.javaica.avp.submission.checkpoint.CheckpointSubmissionStatus;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class GradedCheckpoint extends Checkpoint {

    CheckpointSubmissionStatus status;
    Integer points;

    public GradedCheckpoint(Long id,
                            Long stageId,
                            String name,
                            String description,
                            List<CheckpointBlock> blocks,
                            CheckpointSubmissionStatus status,
                            Integer points) {
        super(id, stageId, name, description, blocks);
        this.status = status;
        this.points = points;
    }
}
