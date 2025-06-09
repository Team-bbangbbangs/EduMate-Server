package com.edumate.eduserver.studentrecord.service;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.MemberStudentRecordNotFoundException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;
import com.edumate.eduserver.studentrecord.repository.MemberStudentRecordRepository;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;
import com.edumate.eduserver.studentrecord.service.dto.StudentRecordOverviewDto;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentRecordService {

    private static final Pattern SEMESTER_PATTERN = Pattern.compile("^\\d{4}-[1-2]$");

    private final StudentRecordDetailRepository studentRecordDetailRepository;
    private final MemberStudentRecordRepository memberStudentRecordRepository;

    @Transactional
    public void update(final long recordId, final String description, final int byteCount) {
        StudentRecordDetail existingDetail = findRecordDetailById(recordId);
        existingDetail.updateContent(description, byteCount);
    }

    public StudentRecordDetailDto get(final long recordId) {
        StudentRecordDetail recordDetail = findRecordDetailById(recordId);
        return StudentRecordDetailDto.of(recordDetail);
    }

    public List<StudentRecordOverviewDto> getAll(final long memberId, final StudentRecordType recordType, final String semester) {
        validateSemesterPattern(semester);
        MemberStudentRecord memberStudentRecord = findMemberStudentRecord(memberId, recordType, semester);
        List<StudentRecordDetail> studentRecordDetails = findRecordDetails(memberStudentRecord);
        return studentRecordDetails.stream().map(StudentRecordOverviewDto::of).toList();
    }

    private void validateSemesterPattern(final String semester) {
        if (!SEMESTER_PATTERN.matcher(semester).matches()) {
            throw new InvalidSemesterFormatException(StudentRecordErrorCode.INVALID_SEMESTER_FORMAT, semester);
        }
    }

    private MemberStudentRecord findMemberStudentRecord(final long memberId, final StudentRecordType recordType, final String semester) {
        return memberStudentRecordRepository.findByMemberIdAndStudentRecordTypeAndSemester(memberId, recordType, semester)
                .orElseThrow(() -> new MemberStudentRecordNotFoundException(StudentRecordErrorCode.MEMBER_STUDENT_RECORD_NOT_FOUND));
    }

    private StudentRecordDetail findRecordDetailById(final long recordId) {
        return studentRecordDetailRepository.findById(recordId)
                .orElseThrow(() -> new StudentRecordDetailNotFoundException(StudentRecordErrorCode.STUDENT_RECORD_DETAIL_NOT_FOUND));
    }

    private List<StudentRecordDetail> findRecordDetails(final MemberStudentRecord memberStudentRecord) {
        return studentRecordDetailRepository.findAllByMemberStudentRecordOrderByCreatedAtAsc(memberStudentRecord);
    }
}
