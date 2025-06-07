package com.edumate.eduserver.studentrecord.facade;

import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentRecordFacade {

    private final StudentRecordService studentRecordService;

    @Transactional
    public void createStudentRecord(final long memberId, final StudentRecordType recordType, final long studentId,
                                    final StudentRecordCreateRequest request) {
        studentRecordService.create(memberId, recordType, studentId, request.semester(), request.description(), request.byteCount());

    }
}
