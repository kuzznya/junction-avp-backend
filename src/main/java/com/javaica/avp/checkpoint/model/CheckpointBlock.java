package com.javaica.avp.checkpoint.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.javaica.avp.common.ContentBlockType;
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
