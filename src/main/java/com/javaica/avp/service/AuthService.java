package com.javaica.avp.service;

import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.AppUser;
import com.javaica.avp.model.AuthRequest;
import com.javaica.avp.model.AuthResponse;
import com.javaica.avp.model.AuthToken;
import com.javaica.avp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthResponse authenticate(AuthRequest request) {
        AppUser user = userService.findUser(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User with username " + request.getUsername() + " not found"));
        if (!encoder.matches(request.getPassword(), user.getPassword()))
            throw new ForbiddenException("Forbidden: invalid password");
        AuthToken authToken = AuthToken.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
        String jwt = jwtUtil.createToken(authToken);
        return new AuthResponse(jwt, user.getRole());
    }
}
