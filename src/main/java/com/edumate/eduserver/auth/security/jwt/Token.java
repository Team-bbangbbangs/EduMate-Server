package com.edumate.eduserver.auth.security.jwt;

public record Token(
        String accessToken,
        String refreshToken
) {
    public static Token of(final String accessToken, final String refreshToken) {
        return new Token(accessToken, refreshToken);
    }
}
