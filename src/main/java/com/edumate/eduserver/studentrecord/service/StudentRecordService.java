package com.edumate.eduserver.studentrecord.service;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.MemberStudentRecordNotFoundException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import com.edumate.eduserver.studentrecord.repository.MemberStudentRecordRepository;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentRecordService {

    private final StudentRecordDetailRepository studentRecordDetailRepository;
    private final MemberStudentRecordRepository memberStudentRecordRepository;

    @Transactional
    public void update(final long memberId, final StudentRecordType recordType, final long studentId,
                       final String semester, final String description, final int byteCount) {
        MemberStudentRecord memberStudentRecord = findStudentRecordByMemberAndTypeAndSemester(memberId, recordType, semester);
        StudentRecordDetail existingDetail = findRecordDetailByStudentIdAndRecordId(studentId, memberStudentRecord.getId());
        existingDetail.updateContent(description, byteCount);
    }

    private MemberStudentRecord findStudentRecordByMemberAndTypeAndSemester(final long memberId,
                                                                            final StudentRecordType recordType,
                                                                            final String semester) {
        return memberStudentRecordRepository.findByMemberIdAndStudentRecordTypeAndSemester(memberId, recordType, semester.trim())
                .orElseThrow(() -> new MemberStudentRecordNotFoundException(StudentRecordErrorCode.MEMBER_STUDENT_RECORD_NOT_FOUND));
    }

    private StudentRecordDetail findRecordDetailByStudentIdAndRecordId(final long studentId,
                                                                       final long memberStudentRecordId) {
        return studentRecordDetailRepository.findByIdAndMemberStudentRecordId(studentId, memberStudentRecordId)
                .orElseThrow(() -> new StudentRecordDetailNotFoundException(StudentRecordErrorCode.STUDENT_RECORD_DETAIL_NOT_FOUND));
    }
}
