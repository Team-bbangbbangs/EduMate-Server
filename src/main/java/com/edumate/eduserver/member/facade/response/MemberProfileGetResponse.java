package com.edumate.eduserver.member.facade.response;

public record MemberProfileGetResponse(
        String email,
        String subject,
        boolean isTeacherVerified,
        String school,
        String nickname
) {
    public static MemberProfileGetResponse of(final String email,
                                              final String subject,
                                              final boolean isTeacherVerified,
                                              final String school,
                                              final String nickname) {
        return new MemberProfileGetResponse(email, subject, isTeacherVerified, school, nickname);
    }
}
