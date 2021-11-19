package com.javaica.avp.entity;

import com.javaica.avp.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Email;

@Table("app_user")
@Value
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    Long id;
    @With
    UserRole role;
    String username;
    String name;
    String surname;
    @Email
    String email;
    @With
    String password;
    @With
    Long teamId;
}
