package com.javaica.avp.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@AllArgsConstructor
@Builder
public class CheckpointSubmissionResult {
    Long id;
    Long teamId;
    Map<Long, JsonNode> content;
    CheckpointSubmissionStatus status;
    String review;
    Integer points;
}
