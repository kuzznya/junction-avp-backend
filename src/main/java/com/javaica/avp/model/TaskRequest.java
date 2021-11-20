package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class TaskRequest {
    Long stageId;
    String name;
    String description;
    List<TaskBlockRequest> blocks;
}
