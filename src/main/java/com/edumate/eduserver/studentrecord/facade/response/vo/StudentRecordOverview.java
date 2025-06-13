package com.edumate.eduserver.studentrecord.facade.response.vo;

public record StudentRecordOverview(
        long recordDetailId,
        String studentNumber,
        String studentName,
        String description
) {
    public static StudentRecordOverview of(final long recordDetailId, final String studentNumber, final String studentName, final String description) {
        return new StudentRecordOverview(recordDetailId, studentNumber, studentName, description);
    }
}
