package com.edumate.eduserver.studentrecord.controller.request.vo;

public record StudentRecordCreateInfo(
        String studentNumber,
        String studentName,
        String description,
        int byteCount
) {
    public static StudentRecordCreateInfo of(final String studentNumber,
                                             final String studentName,
                                             final String description,
                                             final int byteCount) {
        return new StudentRecordCreateInfo(studentNumber, studentName, description, byteCount);
    }
}
