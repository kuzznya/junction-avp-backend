package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class TaskBlockRequest {
    @NotNull
    @NotEmpty
    String content;
    ContentBlockType type;
    JsonNode answer;
}
