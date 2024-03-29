package com.javaica.avp.security;

import com.javaica.avp.user.AppUser;
import com.javaica.avp.user.UserRole;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Value
@Builder
public class AuthToken {

    private static final String ROLE_PREFIX = "ROLE_";

    long id;
    String username;
    UserRole role;

    public Authentication createAuthentication(Supplier<AppUser> principalProvider) {
        List<GrantedAuthority> authorities = role.getPrecedingAndCurrent().stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.name()))
                .collect(Collectors.toList());

        return new AbstractAuthenticationToken(authorities) {

            private AppUser providedUser;

            @Override
            public Object getCredentials() {
                return AuthToken.this;
            }

            @Override
            public synchronized Object getPrincipal() {
                if (providedUser == null)
                    providedUser = principalProvider.get();
                return providedUser;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }
        };
    }
}
