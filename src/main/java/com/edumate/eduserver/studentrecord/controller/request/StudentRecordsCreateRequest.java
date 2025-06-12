package com.edumate.eduserver.studentrecord.controller.request;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import java.util.List;

public record StudentRecordsCreateRequest(
        String semester,
        List<StudentRecordInfo> studentRecords
) {
}
