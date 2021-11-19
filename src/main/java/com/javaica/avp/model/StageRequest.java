package com.javaica.avp.model;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class StageRequest {
    @NotNull
    Long courseId;
    @NotNull
    @NotEmpty
    String name;
    @NotNull
    @NotEmpty
    String description;
}
