package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.facade.response.vo.StudentRecordOverview;
import java.util.List;

public record StudentRecordOverviewsResponse(
        List<StudentRecordOverview> students
) {
    public static StudentRecordOverviewsResponse of(final List<StudentRecordDetail> studentRecordDetails) {
        List<StudentRecordOverview> students = studentRecordDetails.stream()
                .map(detail -> StudentRecordOverview.of(detail.getId(), detail.getStudentNumber(),
                        detail.getStudentName(), detail.getDescription()))
                .toList();
        return new StudentRecordOverviewsResponse(students);
    }
}
