package com.javaica.avp.controller;

import com.javaica.avp.model.AuthRequest;
import com.javaica.avp.model.AuthResponse;
import com.javaica.avp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/authentication")
    public AuthResponse authenticate(@RequestBody @Valid AuthRequest request) {
        return authService.authenticate(request);
    }
}
