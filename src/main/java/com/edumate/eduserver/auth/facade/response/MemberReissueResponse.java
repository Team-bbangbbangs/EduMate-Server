package com.edumate.eduserver.auth.facade.response;

public record MemberReissueResponse(
        String accessToken
) {
    public static MemberReissueResponse of(final String accessToken) {
        return new MemberReissueResponse(accessToken);
    }
}
