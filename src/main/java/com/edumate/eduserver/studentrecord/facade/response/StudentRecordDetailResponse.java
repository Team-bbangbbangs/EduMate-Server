package com.edumate.eduserver.studentrecord.facade.response;

public record StudentRecordDetailResponse(
        String description,
        int byteCount
) {
    public static StudentRecordDetailResponse of(final String description, final int byteCount) {
        return new StudentRecordDetailResponse(description, byteCount);
    }
}
