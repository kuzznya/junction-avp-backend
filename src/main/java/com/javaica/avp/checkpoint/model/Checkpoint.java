package com.javaica.avp.checkpoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Value
@AllArgsConstructor
@SuperBuilder
@NonFinal
public class Checkpoint {
    @With
    Long id;
    Long stageId;
    String name;
    String description;
    @Builder.Default
    List<CheckpointBlock> blocks = new ArrayList<>();
}
