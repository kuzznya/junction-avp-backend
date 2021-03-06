package com.javaica.avp.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class GradedTask extends Task {

    Integer points;

    public GradedTask(Long id,
                      Long stageId,
                      String name,
                      String description,
                      Integer index,
                      List<TaskBlock> blocks,
                      Integer points) {
        super(id, stageId, name, description, index, blocks);
        this.points = points;
    }
}
