package com.edumate.eduserver.auth.facade.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record MemberReissueResponse(
        String accessToken,
        @JsonIgnore
        String refreshToken,
        boolean isAdmin
) {
    public static MemberReissueResponse of(final String accessToken, final String refreshToken, final boolean isAdmin) {
        return new MemberReissueResponse(accessToken, refreshToken, isAdmin);
    }
}
