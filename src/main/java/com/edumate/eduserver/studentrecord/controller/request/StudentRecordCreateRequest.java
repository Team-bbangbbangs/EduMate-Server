package com.edumate.eduserver.studentrecord.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentRecordCreateRequest(
        @NotBlank
        String semester,
        @NotNull
        String description,
        @Min(0)
        int byteCount
) {
}
