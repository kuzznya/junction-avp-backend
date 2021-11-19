package com.javaica.avp.model;

import lombok.Value;

import java.util.List;

@Value
public class TaskRequest {
    Long stageId;
    String name;
    String description;
    List<TaskBlockRequest> blocks;
}
