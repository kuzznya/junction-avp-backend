package com.javaica.avp.course;

import com.fasterxml.jackson.annotation.JsonCreator;
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
