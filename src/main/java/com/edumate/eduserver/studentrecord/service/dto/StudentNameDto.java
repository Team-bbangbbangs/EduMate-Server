package com.edumate.eduserver.studentrecord.service.dto;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import java.util.List;

public record StudentNameDto(
        List<String> studentNames
) {
    public static StudentNameDto of(final List<StudentRecordDetail> studentRecordDetails) {
        List<String> studentNames = studentRecordDetails.stream()
                .map(StudentRecordDetail::getName)
                .toList();
        return new StudentNameDto(studentNames);
    }
}
