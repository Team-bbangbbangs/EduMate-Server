package com.edumate.eduserver.auth.facade.response;

public record MemberLoginResponse(
        String accessToken,
        boolean isAdmin
) {
    public static MemberLoginResponse of(final String accessToken, final boolean isAdmin) {
        return new MemberLoginResponse(accessToken, isAdmin);
    }
}
