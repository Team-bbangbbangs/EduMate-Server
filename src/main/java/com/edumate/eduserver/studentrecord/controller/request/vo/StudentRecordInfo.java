package com.edumate.eduserver.studentrecord.controller.request.vo;

import jakarta.validation.constraints.NotNull;

public record StudentRecordInfo(
        @NotNull(message = "학번을 입력해주세요.")
        String studentNumber,
        @NotNull(message = "학생 이름을 입력해주세요.")
        String studentName
) {
    public static StudentRecordInfo of(final String studentNumber, final String studentName) {
        return new StudentRecordInfo(studentNumber, studentName);
    }
}
