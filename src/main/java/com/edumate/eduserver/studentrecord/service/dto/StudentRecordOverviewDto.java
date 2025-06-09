package com.edumate.eduserver.studentrecord.service.dto;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;

public record StudentRecordOverviewDto(
        long recordDetailId,
        String studentNumber,
        String studentName,
        String description
) {
    public static StudentRecordOverviewDto of(final StudentRecordDetail recordDetail) {
        return new StudentRecordOverviewDto(
                recordDetail.getId(),
                recordDetail.getStudentNumber(),
                recordDetail.getName(),
                recordDetail.getDescription()
        );
    }
}
