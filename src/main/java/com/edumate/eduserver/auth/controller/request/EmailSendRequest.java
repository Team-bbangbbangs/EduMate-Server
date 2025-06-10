package com.edumate.eduserver.auth.controller.request;

import jakarta.validation.constraints.NotNull;

public record EmailSendRequest(
        @NotNull(message = "회원 UUID는 필수입니다.")
        String memberUuid
) {
}
