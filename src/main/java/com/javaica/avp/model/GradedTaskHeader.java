package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GradedTaskHeader extends TaskHeader {
    Integer points;

    public GradedTaskHeader(Long id, String name, String description, Integer index, Integer points) {
        super(id, name, description, index);
        this.points = points;
    }
}
