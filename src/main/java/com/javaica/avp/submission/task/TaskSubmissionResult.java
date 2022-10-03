package com.javaica.avp.submission.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class TaskSubmissionResult {
    Long id;
    Long teamId;
    Map<Long, JsonNode> content;
    int points;
    Instant submissionTimestamp;
}
