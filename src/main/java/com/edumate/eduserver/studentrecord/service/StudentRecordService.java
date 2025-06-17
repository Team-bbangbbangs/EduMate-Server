package com.edumate.eduserver.studentrecord.service;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordCreateInfo;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.AlreadyExistingRecordException;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.MemberStudentRecordNotFoundException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.exception.UpdatePermissionDeniedException;
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
    public void update(final long memberId, final long recordId, final String description, final int byteCount) {
        StudentRecordDetail existingDetail = getRecordDetailById(recordId);
        validatePermission(existingDetail.getMemberStudentRecord(), memberId);
        existingDetail.updateContent(description, byteCount);
    }

    public StudentRecordDetail getRecordDetail(final long memberId, final long recordId) {
        StudentRecordDetail existingDetail = getRecordDetailById(recordId);
        validatePermission(existingDetail.getMemberStudentRecord(), memberId);
        return existingDetail;
    }

    private StudentRecordDetail getRecordDetailById(final long recordId) {
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
                .map(studentRecord -> StudentRecordDetail.create(memberStudentRecord, studentRecord.studentNumber(),
                        studentRecord.studentName(),
                        INITIAL_DESCRIPTION, INITIAL_BYTE_COUNT))
                .toList();
        studentRecordDetailRepository.saveAll(details);
    }

    @Transactional
    public StudentRecordDetail createStudentRecord(final long memberId, final StudentRecordType recordType,
                                                   final String semester,
                                                   final StudentRecordCreateInfo studentRecordCreateInfo) {
        validateSemesterPattern(semester);
        MemberStudentRecord memberStudentRecord = getMemberStudentRecord(memberId, recordType, semester);
        StudentRecordDetail studentRecordDetail = StudentRecordDetail.create(memberStudentRecord,
                studentRecordCreateInfo.studentNumber(), studentRecordCreateInfo.studentName(),
                studentRecordCreateInfo.description(), studentRecordCreateInfo.byteCount());
        return studentRecordDetailRepository.save(studentRecordDetail);
    }

    @Transactional
    public void updateStudentRecordOverview(final long memberId, final long recordId, final String studentNumber,
                                            final String studentName, final String description, final int byteCount) {
        StudentRecordDetail existingDetail = getRecordDetailById(recordId);
        validatePermission(existingDetail.getMemberStudentRecord(), memberId);
        existingDetail.update(studentNumber, studentName, description, byteCount);
    }

    @Transactional
    public void deleteStudentRecord(final long memberId, final long recordId) {
        StudentRecordDetail existingDetail = getRecordDetailById(recordId);
        validatePermission(existingDetail.getMemberStudentRecord(), memberId);
        studentRecordDetailRepository.deleteById(recordId);
    }

    @Transactional
    public void createSemesterRecord(final Member member, final StudentRecordType recordType, final String semester) {
        validateSemesterPattern(semester);
        validateExistingRecord(member.getId(), recordType, semester);
        MemberStudentRecord studentRecord = MemberStudentRecord.create(member, recordType, semester);
        memberStudentRecordRepository.save(studentRecord);
    }

    public MemberStudentRecord getLatestMemberStudentRecord(final long memberId) {
        return memberStudentRecordRepository.findTopByMemberIdOrderByIdDesc(memberId)
                .orElseThrow(() -> new MemberStudentRecordNotFoundException(
                        StudentRecordErrorCode.MEMBER_STUDENT_RECORD_NOT_FOUND));
    }

    private void validateExistingRecord(final long memberId, final StudentRecordType recordType, final String semester) {
        memberStudentRecordRepository.findByMemberIdAndStudentRecordTypeAndSemester(memberId, recordType, semester)
                .ifPresent(record -> {
                    throw new AlreadyExistingRecordException(StudentRecordErrorCode.RECORD_ALREADY_EXISTS);
                });
    }

    private void validatePermission(final MemberStudentRecord memberRecord, final long memberId) {
        if (memberRecord.getMember().getId() != memberId) {
            throw new UpdatePermissionDeniedException(StudentRecordErrorCode.UPDATE_PERMISSION_DENIED);
        }
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
