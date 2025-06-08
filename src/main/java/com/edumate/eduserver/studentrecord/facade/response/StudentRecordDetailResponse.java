package com.edumate.eduserver.studentrecord.facade.response;

public record StudentRecordDetailResponse(
        long recordDetailId,
        String description,
        int byteCount
) {
    public static StudentRecordDetailResponse of(final long recordDetailId, final String description, final int byteCount) {
        return new StudentRecordDetailResponse(recordDetailId, description, byteCount);
    }
}
