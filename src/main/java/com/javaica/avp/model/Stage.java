package com.javaica.avp.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Stage {
    Long id;
    Long courseId;
    String name;
    String description;
    List<GradedTask> tasks;
    GradedCheckpoint checkpoint;
    Integer index;
}
