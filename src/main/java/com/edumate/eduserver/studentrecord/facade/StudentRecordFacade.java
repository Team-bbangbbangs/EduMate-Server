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
    public void updateStudentRecord(final long memberId, final StudentRecordType recordType, final long recordId,
                                    final StudentRecordCreateRequest request) {
        studentRecordService.update(memberId, recordType, recordId, request.semester().trim(), request.description().trim(), request.byteCount());

    }
}
