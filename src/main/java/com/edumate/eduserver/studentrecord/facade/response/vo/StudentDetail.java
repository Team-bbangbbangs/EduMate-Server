package com.edumate.eduserver.studentrecord.facade.response.vo;

public record StudentDetail(
        long recordId,
        String studentName
) {
    public static StudentDetail of(final long recordId, final String studentName) {
        return new StudentDetail(recordId, studentName);
    }
}
