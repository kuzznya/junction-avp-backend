package com.javaica.avp.security;

import com.javaica.avp.config.props.SecurityProperties;
import com.javaica.avp.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final SecurityProperties securityProperties;

    public AuthToken readToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(securityProperties.getSecret().getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
        long id = Long.parseLong(claims.getSubject());
        String username = claims.get("username", String.class);
        if (username == null)
            throw new IllegalArgumentException("username is null");
        UserRole role = UserRole.valueOf(claims.get("role", String.class));
        return AuthToken.builder()
                .id(id)
                .username(username)
                .role(role)
                .build();
    }

    public String createToken(AuthToken data) {
        return Jwts.builder()
                .setSubject(String.valueOf(data.getId()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(
                        Instant.now().plus(securityProperties.getTokenTtl()))
                )
                .claim("username", data.getUsername())
                .claim("role", data.getRole())
                .signWith(Keys.hmacShaKeyFor(securityProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
