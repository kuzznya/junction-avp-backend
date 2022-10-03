package com.javaica.avp.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Value
@SuperBuilder
@AllArgsConstructor
@NonFinal
public class Task {
    Long id;
    Long stageId;
    String name;
    String description;
    Integer index;
    @Builder.Default
    List<TaskBlock> blocks = new ArrayList<>();
}
