package com.edumate.eduserver.studentrecord.controller.request;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordCreateInfo;
import jakarta.validation.constraints.NotNull;

public record StudentRecordCreateRequest(
        @NotNull(message = "학기 정보는 필수입니다.")
        String semester,
        @NotNull(message = "추가할 생기부는 필수입니다.")
        StudentRecordCreateInfo studentRecord
) {
}
