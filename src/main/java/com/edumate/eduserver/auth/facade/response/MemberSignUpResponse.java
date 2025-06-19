package com.edumate.eduserver.auth.facade.response;

public record MemberSignUpResponse(
        String accessToken
) {
    public static MemberSignUpResponse of(final String accessToken) {
        return new MemberSignUpResponse(accessToken);
    }
}
