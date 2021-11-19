package com.javaica.avp.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Stage {
    String name;
    String description;
    List<TaskHeader> tasks;
    CheckpointHeader checkpointHeader;
}
