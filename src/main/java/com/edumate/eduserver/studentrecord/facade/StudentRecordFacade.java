package com.edumate.eduserver.studentrecord.facade;

import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;
import com.edumate.eduserver.studentrecord.service.dto.StudentRecordOverviewDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentRecordFacade {

    private final StudentRecordService studentRecordService;

    @Transactional
    public void updateStudentRecord(final long memberId, final long recordId, final StudentRecordCreateRequest request) {
        studentRecordService.update(recordId, request.description().trim(), request.byteCount());
    }

    public StudentRecordDetailResponse getStudentRecord(final long memberId, final long recordId) {
        StudentRecordDetailDto recordDetailDto = studentRecordService.get(recordId);
        return StudentRecordDetailResponse.of(recordDetailDto.recordDetailId(), recordDetailDto.description(), recordDetailDto.byteCount());
    }

    public StudentRecordOverviewsResponse getStudentRecordOverviews(final long memberId, final StudentRecordType recordType, final String semester) {
        List<StudentRecordOverviewDto> recordOverviewDto = studentRecordService.getAll(memberId, recordType, semester.trim());
        return StudentRecordOverviewsResponse.of(recordOverviewDto);
    }
}
