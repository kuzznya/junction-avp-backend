package com.javaica.avp.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Task {
    Long id;
    Long stageId;
    String name;
    String description;
    Integer index;
    List<TaskBlock> blocks;
}
