package com.javaica.avp.stage;

import com.javaica.avp.submission.checkpoint.CheckpointSubmissionStatus;
import lombok.Value;

@Value
public class StageHeader {
    Long id;
    String name;
    String description;
    CheckpointSubmissionStatus status;
}
