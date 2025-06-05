package com.edumate.eduserver.auth.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtGenerator jwtGenerator;

    public Token issueTokens(final long userId, final String role) {
        return Token.of(generateAccessToken(userId, role), generateRefreshToken(userId, role));
    }

    private String generateAccessToken(final long userId, final String role) {
        return jwtGenerator.generateToken(userId, role, true);
    }

    private String generateRefreshToken(final long userId, final String role) {
        return jwtGenerator.generateToken(userId, role, false);
    }
}
