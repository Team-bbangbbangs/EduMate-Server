package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;
import java.util.List;

public record StudentRecordsResponse(
        List<StudentRecordsDetailResponse> students
) {
    public static StudentRecordsResponse of(List<StudentRecordDetailDto> studentDtos) {
        return new StudentRecordsResponse(
                studentDtos.stream().map(StudentRecordsDetailResponse::of).toList()
        );
    }
}
