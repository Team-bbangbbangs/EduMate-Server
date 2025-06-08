package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;

public record StudentRecordsDetailResponse(
        long recordDetailId,
        String studentNumber,
        String studentName,
        String description
) {
    public static StudentRecordsDetailResponse of(StudentRecordDetailDto studentRecordDetailDto) {
        return new StudentRecordsDetailResponse(
                studentRecordDetailDto.recordDetailId(),
                studentRecordDetailDto.studentNumber(),
                studentRecordDetailDto.studentName(),
                studentRecordDetailDto.description()
        );
    }
}
