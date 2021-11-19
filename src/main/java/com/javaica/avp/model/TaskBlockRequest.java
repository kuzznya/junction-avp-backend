package com.javaica.avp.model;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
public class TaskBlockRequest {
    @NotNull
    @NotEmpty
    String content;
    TaskBlockType type;
}
