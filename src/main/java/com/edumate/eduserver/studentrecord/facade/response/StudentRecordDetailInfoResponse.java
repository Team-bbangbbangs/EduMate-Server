package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;

public record StudentRecordDetailInfoResponse(
        long recordDetailId,
        String studentNumber,
        String studentName,
        String description
) {
    public static StudentRecordDetailInfoResponse of(StudentRecordDetailDto studentRecordDetailDto) {
        return new StudentRecordDetailInfoResponse(
                studentRecordDetailDto.recordDetailId(),
                studentRecordDetailDto.studentNumber(),
                studentRecordDetailDto.studentName(),
                studentRecordDetailDto.description()
        );
    }
}
