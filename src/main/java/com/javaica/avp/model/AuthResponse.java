package com.javaica.avp.model;

import lombok.Value;

@Value
public class AuthResponse {
    String token;
    UserRole role;
}
