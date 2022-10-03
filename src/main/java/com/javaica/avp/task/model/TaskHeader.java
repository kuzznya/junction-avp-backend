package com.javaica.avp.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@AllArgsConstructor
@Builder
@NonFinal
public class TaskHeader {
    Long id;
    String name;
    String description;
    Integer index;
}
