package com.edumate.eduserver.studentrecord.controller.request;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record StudentRecordsCreateRequest(
        @NotNull(message = "학기 정보는 필수입니다.")
        String semester,
        @NotNull(message = "추가할 생기부 목록 리스트는 필수입니다.")
        List<StudentRecordInfo> studentRecords
) {
}
