package com.edumate.eduserver.member.controller.request;

import jakarta.validation.constraints.NotNull;

public record MemberProfileUpdateRequest(
        @NotNull(message = "과목은 필수 입력값입니다.")
        String subject,
        @NotNull(message = "학교는 필수 입력값입니다.")
        String school,
        @NotNull(message = "닉네임은 필수 입력값입니다.")
        String nickname
) {
}
