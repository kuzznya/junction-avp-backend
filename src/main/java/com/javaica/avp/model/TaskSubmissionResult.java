package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class TaskSubmissionResult {
    Long id;
    Long teamId;
    JsonNode content;
    int points;
}
