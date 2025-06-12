package com.edumate.eduserver.studentrecord.service;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.MemberStudentRecordNotFoundException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import com.edumate.eduserver.studentrecord.repository.MemberStudentRecordRepository;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentRecordService {

    private final StudentRecordDetailRepository studentRecordDetailRepository;
    private final MemberStudentRecordRepository memberStudentRecordRepository;

    private static final Pattern SEMESTER_PATTERN = Pattern.compile("^\\d{4}-[1-2]$");
    private static final String INITIAL_DESCRIPTION = "";
    private static final int INITIAL_BYTE_COUNT = 0;

    @Transactional
    public void update(final long recordId, final String description, final int byteCount) {
        StudentRecordDetail existingDetail = getRecordDetailById(recordId);
        existingDetail.updateContent(description, byteCount);
    }

    public StudentRecordDetail getRecordDetailById(final long recordId) {
        return studentRecordDetailRepository.findById(recordId)
                .orElseThrow(() -> new StudentRecordDetailNotFoundException(
                        StudentRecordErrorCode.STUDENT_RECORD_DETAIL_NOT_FOUND));
    }

    public List<StudentRecordDetail> getAll(final long memberId, final StudentRecordType recordType,
                                            final String semester) {
        validateSemesterPattern(semester);
        MemberStudentRecord memberStudentRecord = getMemberStudentRecord(memberId, recordType, semester);
        return findRecordDetails(memberStudentRecord);
    }

    public List<StudentRecordDetail> getStudentNames(final long memberId, final StudentRecordType recordType,
                                                     final String semester) {
        validateSemesterPattern(semester);
        MemberStudentRecord memberStudentRecord = getMemberStudentRecord(memberId, recordType, semester);
        return findRecordDetails(memberStudentRecord);
    }

    @Transactional
    public void createStudentRecords(final long memberId, final StudentRecordType recordType, final String semester,
                                     final List<StudentRecordInfo> studentRecordInfos) {
        MemberStudentRecord memberStudentRecord = getMemberStudentRecord(memberId, recordType, semester);
        List<StudentRecordDetail> details = studentRecordInfos.stream()
                .map(studentRecord -> StudentRecordDetail.create(memberStudentRecord, studentRecord.studentNumber(), studentRecord.studentName(),
                        INITIAL_DESCRIPTION, INITIAL_BYTE_COUNT))
                .toList();
        studentRecordDetailRepository.saveAll(details);
    }

    private void validateSemesterPattern(final String semester) {
        if (!SEMESTER_PATTERN.matcher(semester).matches()) {
            throw new InvalidSemesterFormatException(StudentRecordErrorCode.INVALID_SEMESTER_FORMAT, semester);
        }
    }

    private MemberStudentRecord getMemberStudentRecord(final long memberId, final StudentRecordType recordType,
                                                       final String semester) {
        return memberStudentRecordRepository.findByMemberIdAndStudentRecordTypeAndSemester(memberId, recordType,
                        semester)
                .orElseThrow(() -> new MemberStudentRecordNotFoundException(
                        StudentRecordErrorCode.MEMBER_STUDENT_RECORD_NOT_FOUND));
    }

    private List<StudentRecordDetail> findRecordDetails(final MemberStudentRecord memberStudentRecord) {
        return studentRecordDetailRepository.findAllByMemberStudentRecordOrderByCreatedAtAsc(memberStudentRecord);
    }
}
