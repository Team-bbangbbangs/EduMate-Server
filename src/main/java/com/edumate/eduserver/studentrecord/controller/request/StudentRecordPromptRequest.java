package com.edumate.eduserver.studentrecord.controller.request;

import jakarta.validation.constraints.NotNull;

public record StudentRecordPromptRequest(
        @NotNull(message = "필수 입력값입니다.")
        String prompt
) {
}
