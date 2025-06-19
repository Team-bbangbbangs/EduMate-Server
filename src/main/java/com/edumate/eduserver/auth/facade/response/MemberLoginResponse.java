package com.edumate.eduserver.auth.facade.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record MemberLoginResponse(
        String accessToken,
        @JsonIgnore
        String refreshToken,
        boolean isAdmin
) {
    public static MemberLoginResponse of(final String accessToken, final String refreshToken, final boolean isAdmin) {
        return new MemberLoginResponse(accessToken, refreshToken, isAdmin);
    }
}
