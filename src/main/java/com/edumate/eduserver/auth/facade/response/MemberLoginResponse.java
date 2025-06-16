package com.edumate.eduserver.auth.facade.response;

public record MemberLoginResponse(
        String accessToken,
        String refreshToken
) {
    public static MemberLoginResponse of(final String accessToken, final String refreshToken) {
        return new MemberLoginResponse(accessToken, refreshToken);
    }
}
