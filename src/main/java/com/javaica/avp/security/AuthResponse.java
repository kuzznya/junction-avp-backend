package com.javaica.avp.security;

import com.javaica.avp.user.UserRole;
import lombok.Value;

@Value
public class AuthResponse {
    String token;
    UserRole role;
}
