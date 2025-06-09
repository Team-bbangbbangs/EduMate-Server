package com.edumate.eduserver.studentrecord.service.dto;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;

public record StudentRecordDetailDto(
        long recordDetailId,
        String description,
        int byteCount
) {
    public static StudentRecordDetailDto of(final StudentRecordDetail recordDetail) {
        return new StudentRecordDetailDto(
                recordDetail.getId(),
                recordDetail.getDescription(),
                recordDetail.getByteCount()
        );
    }
}
