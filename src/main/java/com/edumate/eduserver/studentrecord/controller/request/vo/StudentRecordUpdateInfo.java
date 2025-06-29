package com.edumate.eduserver.studentrecord.controller.request.vo;

public record StudentRecordUpdateInfo(
        String studentNumber,
        String studentName,
        String description,
        int byteCount
) {
    public static StudentRecordUpdateInfo of(final String studentNumber,
                                             final String studentName,
                                             final String description,
                                             final int byteCount) {
        return new StudentRecordUpdateInfo(studentNumber, studentName, description, byteCount);
    }
}
