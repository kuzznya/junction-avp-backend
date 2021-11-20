package com.javaica.avp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class TaskBlock {
    Long id;
    String content;
    TaskBlockType type;
    Integer index;
}
