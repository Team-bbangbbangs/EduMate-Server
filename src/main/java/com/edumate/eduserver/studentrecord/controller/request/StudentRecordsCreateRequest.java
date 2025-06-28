package com.edumate.eduserver.studentrecord.controller.request;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record StudentRecordsCreateRequest(
        @NotNull(message = "학기 정보는 필수입니다.")
        String semester,
        @Size(min = 1, message = "최소 1명 이상의 학생 정보를 입력해주세요.")
        @NotNull(message = "추가할 생기부 목록 리스트는 필수입니다.")
        List<@Valid StudentRecordInfo> studentRecords
) {
}
