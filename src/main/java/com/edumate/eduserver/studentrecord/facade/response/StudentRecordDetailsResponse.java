package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;
import java.util.List;

public record StudentRecordDetailsResponse(
        List<StudentRecordDetailInfoResponse> students
) {
    public static StudentRecordDetailsResponse of(List<StudentRecordDetailDto> studentRecordDetailDto) {
        return new StudentRecordDetailsResponse(
                studentRecordDetailDto.stream().map(StudentRecordDetailInfoResponse::of).toList()
        );
    }
}
