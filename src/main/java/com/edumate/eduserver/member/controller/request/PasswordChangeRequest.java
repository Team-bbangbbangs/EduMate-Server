package com.edumate.eduserver.member.controller.request;

import jakarta.validation.constraints.NotNull;

public record PasswordChangeRequest(
        @NotNull(message = "현재 비밀번호를 입력해주세요.")
        String currentPassword,
        @NotNull(message = "새로운 비밀번호를 입력해주세요.")
        String newPassword
) {
}
