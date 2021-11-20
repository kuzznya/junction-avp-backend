package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Stage {
    Long id;
    Long courseId;
    String name;
    String description;
    List<GradedTask> tasks;
    GradedCheckpoint checkpoint;
    Integer index;
}
