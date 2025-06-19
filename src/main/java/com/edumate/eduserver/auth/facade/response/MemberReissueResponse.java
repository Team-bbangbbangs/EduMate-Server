package com.edumate.eduserver.auth.facade.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record MemberReissueResponse(
        String accessToken,
        @JsonIgnore
        String refreshToken
) {
    public static MemberReissueResponse of(final String accessToken, final String refreshToken) {
        return new MemberReissueResponse(accessToken, refreshToken);
    }
}
