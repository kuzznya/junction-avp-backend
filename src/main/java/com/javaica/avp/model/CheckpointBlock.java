package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class CheckpointBlock {
    @With
    Long id;
    String content;
    ContentBlockType type;
    @With
    Integer index;
}
