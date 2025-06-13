package com.edumate.eduserver.studentrecord.controller.request.vo;

public record StudentRecordInfo(
        String studentNumber,
        String studentName
) {
    public static StudentRecordInfo of(final String studentNumber, final String studentName) {
        return new StudentRecordInfo(studentNumber, studentName);
    }
}
