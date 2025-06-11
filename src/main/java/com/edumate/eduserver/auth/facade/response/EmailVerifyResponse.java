package com.edumate.eduserver.auth.facade.response;

public record EmailVerifyResponse(
        String accessToken,
        String refreshToken
) {
    public static EmailVerifyResponse of(final String accessToken, final String refreshToken) {
        return new EmailVerifyResponse(accessToken, refreshToken);
    }
}
