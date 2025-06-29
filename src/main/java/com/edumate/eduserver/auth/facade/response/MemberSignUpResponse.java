package com.edumate.eduserver.auth.facade.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record MemberSignUpResponse(
        String accessToken,
        @JsonIgnore
        String refreshToken,
        boolean isAdmin
) {

    private static final boolean DEFAULT_IS_ADMIN = false;

    public static MemberSignUpResponse of(final String accessToken, final String refreshToken) {
        return new MemberSignUpResponse(accessToken, refreshToken, DEFAULT_IS_ADMIN);
    }
}
