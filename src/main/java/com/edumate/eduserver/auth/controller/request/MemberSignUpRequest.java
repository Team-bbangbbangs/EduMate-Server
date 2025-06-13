package com.edumate.eduserver.auth.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record MemberSignUpRequest(
        @NotNull(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotNull(message = "비밀번호는 필수 입력값입니다.")
        String password,
        @NotNull(message = "과목은 필수 입력값입니다.")
        String subject,
        @NotNull(message = "학교는 필수 입력값입니다.")
        String school
) {
}
