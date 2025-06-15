package com.edumate.eduserver.studentrecord.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StudentRecordOverviewUpdateRequest(
        @NotNull(message = "학번은 필수 입력 항목입니다.")
        String studentNumber,
        @NotNull(message = "학생 이름은 필수 입력 항목입니다.")
        String studentName,
        @NotNull(message = "생기부 내용은 필수 입력 항목입니다.")
        String description,
        @Min(value = 0, message = "바이트 수는 0 이상이어야 합니다.")
        int byteCount
) {
}
