package com.edumate.eduserver.auth.facade.response;

public record EmailVerifyResponse(
        long memberId,
        boolean isVerified
) {
    public static EmailVerifyResponse of(final long memberId, final boolean isVerified) {
        return new EmailVerifyResponse(memberId, isVerified);
    }
}
