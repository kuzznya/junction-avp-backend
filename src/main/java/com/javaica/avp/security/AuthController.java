package com.javaica.avp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/authentication")
    public AuthResponse authenticate(@RequestBody @Valid AuthRequest request) {
        return authService.authenticate(request);
    }
}
