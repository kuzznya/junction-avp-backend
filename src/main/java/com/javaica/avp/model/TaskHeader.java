package com.javaica.avp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class TaskHeader {
    Long id;
    String name;
    String description;
    Integer index;
}
