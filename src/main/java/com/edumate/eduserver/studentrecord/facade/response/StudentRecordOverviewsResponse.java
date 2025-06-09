package com.edumate.eduserver.studentrecord.facade.response;

import com.edumate.eduserver.studentrecord.service.dto.StudentRecordOverviewDto;
import java.util.List;

public record StudentRecordOverviewsResponse(
        List<StudentRecordOverviewResponse> students
) {
    public static StudentRecordOverviewsResponse of(final List<StudentRecordOverviewDto> studentRecordDetailDto) {
        return new StudentRecordOverviewsResponse(
                studentRecordDetailDto.stream().map(StudentRecordOverviewResponse::of).toList()
        );
    }
}
