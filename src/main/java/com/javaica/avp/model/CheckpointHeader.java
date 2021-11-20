package com.javaica.avp.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

@Value
@AllArgsConstructor
@SuperBuilder
@NonFinal
public class CheckpointHeader {
    Long id;
    String name;
    String description;
}
