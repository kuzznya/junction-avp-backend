package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class Group {
    @With
    Long id;
    @NotNull
    @Min(1)
    @Max(10)
    Integer complexityLevel;
    @NotNull
    Long courseId;
}
