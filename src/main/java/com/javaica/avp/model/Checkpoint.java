package com.javaica.avp.model;

import lombok.Value;

@Value
public class Checkpoint {
    Long stageId;
    String name;
    String description;
}
