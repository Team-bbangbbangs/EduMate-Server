package com.edumate.eduserver.auth.facade.response;

public record MemberLoginResponse(
        String accessToken,
        String refreshToken,
        boolean isAdmin
) {
    public static MemberLoginResponse of(final String accessToken, final String refreshToken, final boolean isAdmin) {
        return new MemberLoginResponse(accessToken, refreshToken, isAdmin);
    }
}
