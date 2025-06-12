package com.edumate.eduserver.common.security;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {

    private MemberAuthentication(final Object principal, final Object credentials,
                                 final Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication create(final String memberUuid,
                                              final Collection<? extends GrantedAuthority> authorities) {
        return new MemberAuthentication(memberUuid, null, authorities);
    }
}
