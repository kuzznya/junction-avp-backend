package com.javaica.avp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class Team {
    @With
    Long id;
    @NotNull
    Long groupId;
    @NotBlank
    String name;
    List<String> members;
}
