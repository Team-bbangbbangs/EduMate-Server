package com.edumate.eduserver.studentrecord.facade;

import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordsResponse;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;
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
    public void updateStudentRecord(final long memberId, final StudentRecordType recordType, final long recordId,
                                    final StudentRecordCreateRequest request) {
        studentRecordService.update(memberId, recordType, recordId, request.semester().trim(), request.description().trim(), request.byteCount());
    }

    public StudentRecordDetailResponse getStudentRecord(final long memberId, final StudentRecordType recordType, final long recordId, final String semester) {
        StudentRecordDetailDto recordDetailDto = studentRecordService.get(memberId, recordType, recordId, semester.trim());
        return StudentRecordDetailResponse.of(recordDetailDto.recordDetailId(), recordDetailDto.description(), recordDetailDto.byteCount());
    }

    public StudentRecordsResponse getStudentRecords(final long memberId, final StudentRecordType recordType, final String semester) {
        List<StudentRecordDetailDto> recordDetailDtos = studentRecordService.getAll(memberId, recordType, semester.trim());
        return StudentRecordsResponse.of(recordDetailDtos);
    }
}
