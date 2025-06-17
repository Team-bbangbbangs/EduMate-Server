package com.edumate.eduserver.member.facade.response;

public record MemberProfileGetResponse(
        String email,
        String subject,
        boolean isTeacherVerified,
        String school,
        String nickname,
        String semester
) {
    public static MemberProfileGetResponse of(final String email,
                                              final String subject,
                                              final boolean isTeacherVerified,
                                              final String school,
                                              final String nickname,
                                              final String semester) {
        return new MemberProfileGetResponse(email, subject, isTeacherVerified, school, nickname, semester);
    }
}
