package com.javaica.avp.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Task {
    Long id;
    Long stageId;
    String name;
    String description;
}
