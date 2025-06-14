package com.edumate.eduserver.studentrecord.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordCreateInfo;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.exception.InvalidSemesterFormatException;
import com.edumate.eduserver.studentrecord.exception.StudentRecordDetailNotFoundException;
import com.edumate.eduserver.studentrecord.exception.UpdatePermissionDeniedException;
import com.edumate.eduserver.studentrecord.repository.MemberStudentRecordRepository;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import com.edumate.eduserver.util.ServiceTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("생활기록부 서비스 테스트")
class StudentRecordServiceTest extends ServiceTest {

    @Autowired
    private StudentRecordService studentRecordService;

    @Autowired
    private StudentRecordDetailRepository studentRecordDetailRepository;

    @Autowired
    private MemberStudentRecordRepository memberStudentRecordRepository;

    @Test
    @DisplayName("특정 학생의 특정 생기부 항목에 대한 내용을 업데이트 한다.")
    void updateStudentRecordSuccess() {
        // given
        long studentRecordId = defaultRecordDetail.getId();
        long memberId = defaultTeacher.getId();
        String newDescription = "생활기록부 내용이 변경되었습니다. 학생은 매우 성실하고 학습 의욕이 높습니다.";
        int newByteCount = 20;

        // when
        studentRecordService.update(memberId, studentRecordId, newDescription, newByteCount);

        // then
        StudentRecordDetail updated = studentRecordDetailRepository.findById(studentRecordId).get();
        assertThat(updated.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("특정 학생의 특정 생기부 항목에 대한 종합 내용을 불러온다.")
    void getStudentRecordStudentRecordDetail() {
        // given
        long studentRecordId = defaultRecordDetail.getId();
        long memberId = defaultTeacher.getId();

        // when
        StudentRecordDetail recordDetail = studentRecordService.getRecordDetail(memberId, studentRecordId);

        // then
        assertAll(
                () -> assertEquals(studentRecordId, recordDetail.getId()),
                () -> assertEquals(defaultRecordDetail.getDescription(), recordDetail.getDescription()),
                () -> assertEquals(defaultRecordDetail.getByteCount(), recordDetail.getByteCount())
        );
    }

    @Test
    @DisplayName("여러 개의 생기부 항목을 생성한다.")
    void createStudentRecords() {
        // given
        long memberId = 1L;
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        String semester = "2025-1";
        List<StudentRecordInfo> studentRecordInfos = List.of(
                StudentRecordInfo.of("2023002", "김지안"),
                StudentRecordInfo.of("2023003", "유태근")
        );

        // when
        studentRecordService.createStudentRecords(memberId, recordType, semester, studentRecordInfos);

        // then
        MemberStudentRecord memberStudentRecord = memberStudentRecordRepository
                .findByMemberIdAndStudentRecordTypeAndSemester(memberId, recordType, semester)
                .orElseThrow();

        List<StudentRecordDetail> details = studentRecordDetailRepository
                .findAllByMemberStudentRecordOrderByCreatedAtAsc(memberStudentRecord);

        List<String> newNames = List.of("김지안", "유태근");

        List<StudentRecordDetail> newDetails = details.stream()
                .filter(d -> newNames.contains(d.getStudentName()))
                .toList();

        assertAll(
                () -> assertThat(newDetails).hasSize(2),
                () -> assertThat(newDetails)
                        .extracting(StudentRecordDetail::getStudentName)
                        .containsExactlyElementsOf(newNames),
                () -> assertThat(newDetails).allMatch(d -> d.getDescription().isEmpty()),
                () -> assertThat(newDetails).allMatch(d -> d.getByteCount() == 0)
        );
    }

    @Test
    @DisplayName("특정 학기의 특정 생기부 항목에 포함된 학생 이름 목록을 불러온다.")
    void getStudentRecordStudentNames() {
        // given
        long memberId = 1L;
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        String semester = "2025-1";
        // when
        List<StudentRecordDetail> details = studentRecordService.getStudentNames(memberId, recordType, semester);
        List<String> studentNames = details.stream()
                .map(StudentRecordDetail::getStudentName)
                .toList();

        // then
        assertThat(studentNames).contains("김가연", "이승섭");
     }

     @Test
     @DisplayName("잘못된 학기 형식으로 요청을 보낼 경우, 예외가 발생한다.")
     void invalidSemesterFormat() {
         // given
            String invalidSemester = "2025-3";

         // when & then
            assertThatThrownBy(() ->
                    studentRecordService.getStudentNames(1L, StudentRecordType.BEHAVIOR_OPINION, invalidSemester)
            ).isInstanceOf(InvalidSemesterFormatException.class);
      }


    @Test
    @DisplayName("존재하지 않는 학생 상세 기록에 대해 업데이트 시도시 예외가 발생한다.")
    void updateWithNonExistingStudentRecordDetail() {
        // given
        long memberId = defaultTeacher.getId();
        long nonExistingStudentRecordId = 999L;
        String newDescription = "새로운 내용";
        int newByteCount = 10;

        // when & then
        assertThatThrownBy(() ->
                studentRecordService.update(memberId, nonExistingStudentRecordId, newDescription, newByteCount)
        ).isInstanceOf(StudentRecordDetailNotFoundException.class);
    }

    @Test
    @DisplayName("업데이트 권한이 없는 경우 예외가 발생한다.")
    void updateWithPermissionDenied() {
        long otherMemberId = 12L;

        // when & then
        assertThatThrownBy(() ->
                studentRecordService.update(otherMemberId, defaultRecordDetail.getId(), "변경된 내용", 15)
        ).isInstanceOf(UpdatePermissionDeniedException.class);
    }

    @Test
    @DisplayName("생활기록부 한 개를 생성한다.")
    void createStudentRecord() {
        // given
        long memberId = 1L;
        StudentRecordType recordType = StudentRecordType.ABILITY_DETAIL;
        String semester = "2025-1";
        String studentNumber = "2023002";
        String studentName = "유태근";
        String description = "생활기록부 내용";
        int byteCount = 22;
        StudentRecordCreateInfo studentRecordCreateInfo = StudentRecordCreateInfo.of(
                studentNumber, studentName, description, byteCount
        );

        // when
        StudentRecordDetail studentRecord = studentRecordService.createStudentRecord(memberId, recordType, semester,
                studentRecordCreateInfo);

        // then
        MemberStudentRecord memberStudentRecord = memberStudentRecordRepository
                .findByMemberIdAndStudentRecordTypeAndSemester(memberId, recordType, semester)
                .orElseThrow();

        assertAll(
                () -> assertThat(memberStudentRecord.getSemester()).isEqualTo(semester),
                () -> assertThat(studentRecord.getStudentNumber()).isEqualTo(studentNumber),
                () -> assertThat(studentRecord.getStudentName()).isEqualTo(studentName),
                () -> assertThat(studentRecord.getDescription()).isEqualTo(description),
                () -> assertThat(studentRecord.getByteCount()).isEqualTo(byteCount)
        );
    }
}
