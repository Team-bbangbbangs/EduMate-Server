package com.edumate.eduserver.studentrecord.controller.request;

import jakarta.validation.constraints.NotNull;

public record SemesterCreateRequest(
        @NotNull(message = "학기 정보는 필수입니다.")
        String semester
) {
}
