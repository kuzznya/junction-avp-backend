package com.javaica.avp.submission.checkpoint;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class CheckpointReview {
    String review;
    CheckpointSubmissionStatus status;
    Integer points;
}
