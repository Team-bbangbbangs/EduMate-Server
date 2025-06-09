package com.edumate.eduserver.studentrecord.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StudentRecordCreateRequest(
        @NotNull(message = "null 값은 허용되지 않습니다.")
        String description,
        @Min(value = 0, message = "바이트 수는 0 이상이어야 합니다.")
        int byteCount
) {
}
