package com.edumate.eduserver.member.facade.dto;

public record MemberEmailUpdatedEvent(
        String email,
        String memberUuid,
        String verificationCode
) {
    public static MemberEmailUpdatedEvent of(final String email, final String memberUuid, final String verificationCode) {
        return new MemberEmailUpdatedEvent(email, memberUuid, verificationCode);
    }
}
