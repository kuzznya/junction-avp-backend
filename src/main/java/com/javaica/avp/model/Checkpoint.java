package com.javaica.avp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@AllArgsConstructor
@Builder
public class Checkpoint {
    @With
    Long id;
    Long stageId;
    String name;
    String description;
}
