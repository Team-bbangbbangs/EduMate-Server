package com.edumate.eduserver.studentrecord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import com.edumate.eduserver.studentrecord.service.dto.StudentNameDto;
import com.edumate.eduserver.studentrecord.service.dto.StudentRecordDetailDto;
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
        long studentRecordId = defaultRecordDetail.getId();
        String newDescription = "생활기록부 내용이 변경되었습니다. 학생은 매우 성실하고 학습 의욕이 높습니다.";
        int newByteCount = 20;

        // when
        studentRecordService.update(studentRecordId, newDescription, newByteCount);

        // then
        StudentRecordDetail updated = studentRecordDetailRepository.findById(studentRecordId).get();
        assertThat(updated.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("특정 학생의 특정 생기부 항목에 대한 종합 내용을 불러온다.")
    void getStudentRecordDetail() {
        // given
        long studentRecordId = defaultRecordDetail.getId();

        // when
        StudentRecordDetailDto recordDetail = studentRecordService.get(studentRecordId);

        // then
        assertAll(
                () -> assertEquals(studentRecordId, recordDetail.recordDetailId()),
                () -> assertEquals(defaultRecordDetail.getDescription(), recordDetail.description()),
                () -> assertEquals(defaultRecordDetail.getByteCount(), recordDetail.byteCount())
        );
    }

    @Test
    @DisplayName("특정 학기의 특정 생기부 항목에 포함된 학생 이름 목록을 불러온다.")
    void getStudentName() {
        // given
        long memberId = 1L;
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        String semester = "2025-1";
        // when
        StudentNameDto studentNameDto = studentRecordService.getStudentName(memberId, recordType, semester);

        // then
        assertThat(studentNameDto.studentNames()).contains("김가연", "이승섭");
     }

     @Test
     @DisplayName("잘못된 학기 형식으로 요청을 보낼 경우, 예외가 발생한다.")
     void invalidSemesterFormat() {
         // given
            String invalidSemester = "2025-3";

         // when & then
            assertThatThrownBy(() ->
                    studentRecordService.getStudentName(1L, StudentRecordType.BEHAVIOR_OPINION, invalidSemester)
            ).isInstanceOf(InvalidSemesterFormatException.class);
      }


    @Test
    @DisplayName("존재하지 않는 학생 상세 기록에 대해 업데이트 시도시 예외가 발생한다.")
    void updateWithNonExistingStudentRecordDetail() {
        // given
        long nonExistingStudentRecordId = 999L;
        String newDescription = "새로운 내용";
        int newByteCount = 10;

        // when & then
        assertThatThrownBy(() ->
                studentRecordService.update(nonExistingStudentRecordId, newDescription, newByteCount)
        ).isInstanceOf(StudentRecordDetailNotFoundException.class);
    }
}
