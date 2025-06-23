package com.edumate.eduserver.auth.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record PasswordFindRequest(
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @NotNull(message = "이메일 주소를 입력해주세요.")
        String email
) {
}
