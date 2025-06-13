package com.edumate.eduserver.auth.facade.response;

public record MemberSignUpResponse(
        String accessToken,
        String refreshToken
) {
    public static MemberSignUpResponse of(final String accessToken, final String refreshToken) {
        return new MemberSignUpResponse(accessToken, refreshToken);
    }
}
