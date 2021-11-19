package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class Course {
    @With
    Long id;
    @NotBlank
    String name;
    String description;
}
