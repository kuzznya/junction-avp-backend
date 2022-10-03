package com.javaica.avp.stage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.javaica.avp.checkpoint.model.GradedCheckpoint;
import com.javaica.avp.task.model.GradedTask;
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
