package com.edumate.eduserver.auth.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record MemberLoginRequest(
        @NotNull(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotNull(message = "비밀번호는 필수 입력값입니다.")
        String password
) {
}
