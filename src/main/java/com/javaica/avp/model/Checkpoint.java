package com.javaica.avp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class Checkpoint {
    @With
    Long id;
    Long stageId;
    String name;
    String description;
    @Builder.Default
    List<CheckpointBlock> blocks = new ArrayList<>();
}
