package com.edumate.eduserver.member.controller.request;

public record MemberProfileUpdateRequest(
        String subject,
        String school,
        String nickname
) {
}
