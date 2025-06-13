package com.edumate.eduserver.user.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role {
    TEACHER("ROLE_TEACHER"),
    PENDING_TEACHER("ROLE_PENDING_TEACHER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    public static Collection<? extends GrantedAuthority> toGrantedAuthorities(final Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(Role::toGrantedAuthority)
                .collect(Collectors.toList());
    }

    private GrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(name);
    }
}
