package com.edumate.eduserver.studentrecord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.MemberStudentRecordNotFoundException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import com.edumate.eduserver.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("생활기록부 서비스 테스트")
class StudentRecordServiceTest extends ServiceTest {

    @Autowired
    private StudentRecordService studentRecordService;

    @Autowired
    private StudentRecordDetailRepository studentRecordDetailRepository;

    @Test
    @DisplayName("특정 학생의 특정 생기부 항목에 대한 내용을 업데이트 한다.")
    void updateStudentRecordSuccess() {
        // given
        long memberId = defaultTeacher.getId();
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        long studentRecordId = defaultRecordDetail.getId();
        String semester = "2025-1";
        String newDescription = "생활기록부 내용이 변경되었습니다. 학생은 매우 성실하고 학습 의욕이 높습니다.";
        int newByteCount = 20;

        // when
        studentRecordService.update(memberId, recordType, studentRecordId, semester, newDescription, newByteCount);

        // then
        StudentRecordDetail updated = studentRecordDetailRepository.findById(studentRecordId).get();
        assertThat(updated.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("유효하지 않은 학기 형식으로 업데이트 시 예외 발생 테스트")
    void updateWithInvalidSemesterFormat() {
        // given
        long memberId = defaultTeacher.getId();
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        long studentRecordId = defaultRecordDetail.getId();
        String invalidSemester = "2025-3"; // 유효하지 않은 학기 형식 (1 또는 2만 가능)
        String newDescription = "새로운 내용";
        int newByteCount = 10;

        // when & then
        assertThatThrownBy(() ->
                studentRecordService.update(memberId, recordType, studentRecordId, invalidSemester, newDescription,
                        newByteCount)
        ).isInstanceOf(InvalidSemesterFormatException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 아이디로 기록을 업데이트하면 예외가 발생한다.")
    void updateWithNonExistingMemberStudentRecord() {
        // given
        long nonExistingMemberId = 999L;
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        long studentRecordId = defaultRecordDetail.getId();
        String semester = "2025-1";
        String newDescription = "새로운 내용";
        int newByteCount = 10;

        // when & then
        assertThatThrownBy(() ->
                studentRecordService.update(nonExistingMemberId, recordType, studentRecordId, semester, newDescription,
                        newByteCount)
        ).isInstanceOf(MemberStudentRecordNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 학생 상세 기록에 대해 업데이트 시도시 예외가 발생한다.")
    void updateWithNonExistingStudentRecordDetail() {
        // given
        long memberId = defaultTeacher.getId();
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        long nonExistingStudentRecordId = 999L;
        String semester = "2025-1";
        String newDescription = "새로운 내용";
        int newByteCount = 10;

        // when & then
        assertThatThrownBy(() ->
                studentRecordService.update(memberId, recordType, nonExistingStudentRecordId, semester, newDescription,
                        newByteCount)
        ).isInstanceOf(StudentRecordDetailNotFoundException.class);
    }
}
