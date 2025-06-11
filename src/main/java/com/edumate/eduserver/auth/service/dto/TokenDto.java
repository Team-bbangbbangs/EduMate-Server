package com.edumate.eduserver.auth.service.dto;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
    public static TokenDto of(final String accessToken, final String refreshToken) {
        return new TokenDto(accessToken, refreshToken);
    }
}
