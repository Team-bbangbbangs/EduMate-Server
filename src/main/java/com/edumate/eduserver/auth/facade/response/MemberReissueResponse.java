package com.edumate.eduserver.auth.facade.response;

public record MemberReissueResponse(
        String accessToken,
        String refreshToken
) {
    public static MemberReissueResponse of(final String accessToken, final String refreshToken) {
        return new MemberReissueResponse(accessToken, refreshToken);
    }
}
