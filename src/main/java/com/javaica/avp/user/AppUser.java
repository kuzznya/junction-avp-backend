package com.javaica.avp.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class AppUser {
    @With
    Long id;
    @NotBlank
    String username;
    @With
    UserRole role;
    @NotBlank
    String name;
    @NotBlank
    String surname;
    @Email
    @NotBlank
    String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @With
    String password;
    @JsonIgnore
    @With
    Long teamId;
}
