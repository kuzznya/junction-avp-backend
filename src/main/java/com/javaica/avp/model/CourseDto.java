package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CourseDto {
    String name;
    String description;

    public CourseDto(@JsonProperty("name") String name, @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
    }
}
