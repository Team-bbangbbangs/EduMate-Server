package com.edumate.eduserver.member.facade.response;

public record MemberNicknameValidationResponse(
        boolean isInvalid,
        boolean isDuplicated
) {
    public static MemberNicknameValidationResponse of(final boolean isInvalid, final boolean isDuplicated) {
        return new MemberNicknameValidationResponse(isInvalid, isDuplicated);
    }
}
