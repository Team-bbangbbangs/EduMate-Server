package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.facade.response.vo.StudentDetail;
import java.util.List;

public record StudentNamesResponse(
        List<StudentDetail> studentDetails
) {
    public static StudentNamesResponse of(final List<StudentRecordDetail> studentRecordDetails) {
        List<StudentDetail> studentDetails = studentRecordDetails.stream()
                .map(detail -> StudentDetail.of(detail.getId(), detail.getStudentName()))
                .toList();
        return new StudentNamesResponse(studentDetails);
    }
}
