package com.javaica.avp.service;

import com.javaica.avp.config.props.AdminProperties;
import com.javaica.avp.entity.UserEntity;
import com.javaica.avp.model.UserRole;
import com.javaica.avp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class AdminCreator implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdminProperties adminProperties;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        String encodedPassword = encoder.encode(adminProperties.getPassword());
        UserEntity admin = userRepository.findByUsername(adminProperties.getUsername())
                .map(entity -> entity.withPassword(encodedPassword).withRole(UserRole.ADMIN))
                .orElseGet(() -> createAdmin(encodedPassword));
        userRepository.save(admin);
        log.info("Admin account {} created/updated", adminProperties.getUsername());
    }

    private UserEntity createAdmin(String encodedPassword) {
        return UserEntity.builder()
                .username(adminProperties.getUsername())
                .role(UserRole.ADMIN)
                .name("John")
                .surname("Doe")
                .email("admin@javaica.com")
                .password(encodedPassword)
                .build();
    }
}
