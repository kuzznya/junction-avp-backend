package com.javaica.avp.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
public class Stage {
    Long courseId;
    String name;
    String description;
    List<TaskHeader> tasks;
    CheckpointHeader checkpoint;
}
