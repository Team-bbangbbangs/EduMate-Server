package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.service.dto.StudentRecordOverviewDto;

public record StudentRecordOverviewResponse(
        long recordDetailId,
        String studentNumber,
        String studentName,
        String description
) {
    public static StudentRecordOverviewResponse of(final StudentRecordOverviewDto studentRecordOverviewDto) {
        return new StudentRecordOverviewResponse(
                studentRecordOverviewDto.recordDetailId(),
                studentRecordOverviewDto.studentNumber(),
                studentRecordOverviewDto.studentName(),
                studentRecordOverviewDto.description()
        );
    }
}
