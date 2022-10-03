package com.javaica.avp.checkpoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class CheckpointRequest {
    @NotNull
    Long stageId;
    @NotNull
    @NotBlank
    String name;
    String description;
    @NotEmpty
    List<CheckpointBlock> blocks;
}
