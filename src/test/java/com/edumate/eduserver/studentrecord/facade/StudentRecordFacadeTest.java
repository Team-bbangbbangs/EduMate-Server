package com.edumate.eduserver.studentrecord.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordCreateInfo;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StudentRecordFacadeTest {
    @Mock
    MemberService memberService;
    @Mock
    StudentRecordService studentRecordService;
    @InjectMocks
    StudentRecordFacade studentRecordFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("학생 기록 수정이 정상 동작한다.")
    void updateStudentRecord() {
        long memberId = 100L;
        long recordId = 1L;
        String description = "안녕하세요";
        int byteCount = 10;
        willDoNothing().given(studentRecordService).update(memberId, recordId, description, byteCount);
        studentRecordFacade.updateStudentRecord(memberId, recordId, description, byteCount);
        verify(studentRecordService).update(memberId, recordId, description, byteCount);
    }

    @Test
    @DisplayName("학생 기록 상세 조회가 정상 동작한다.")
    void getStudentRecord() {
        long memberId = 200L;
        long recordId = 2L;
        StudentRecordDetail detail = mock(StudentRecordDetail.class);
        given(studentRecordService.getRecordDetail(memberId, recordId)).willReturn(detail);
        given(detail.getDescription()).willReturn("desc");
        given(detail.getByteCount()).willReturn(20);

        StudentRecordDetailResponse response = studentRecordFacade.getStudentRecord(memberId, recordId);

        assertThat(response.description()).isEqualTo("desc");
        assertThat(response.byteCount()).isEqualTo(20);
    }

    @Test
    @DisplayName("학생 기록 오버뷰 조회가 정상 동작한다.")
    void getStudentRecordOverviews() {
        long memberId = 300L;
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-1";
        List<StudentRecordDetail> details = List.of(mock(StudentRecordDetail.class));
        given(studentRecordService.getAll(memberId, type, semester)).willReturn(details);

        StudentRecordOverviewsResponse response = studentRecordFacade.getStudentRecordOverviews(memberId, type,
                semester);

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("학생 이름 목록 조회가 정상 동작한다.")
    void getStudentDetails() {
        long memberId = 400L;
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-2";
        List<StudentRecordDetail> details = List.of(mock(StudentRecordDetail.class));
        given(studentRecordService.getStudentNames(memberId, type, semester)).willReturn(details);

        StudentNamesResponse response = studentRecordFacade.getStudentDetails(memberId, type, semester);

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("학생 기록 생성이 정상 동작한다.")
    void createStudentRecords() {
        long memberId = 500L;
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-2";
        List<StudentRecordInfo> infos = List.of(mock(StudentRecordInfo.class));
        willDoNothing().given(studentRecordService).createStudentRecords(memberId, type, semester, infos);

        studentRecordFacade.createStudentRecords(memberId, type, semester, infos);

        verify(studentRecordService).createStudentRecords(memberId, type, semester, infos);
    }

    @Test
    @DisplayName("학생 기록 생성이 정상 동작한다.")
    void createStudentRecord() {
        long memberId = 500L;
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-2";
        StudentRecordCreateInfo info = mock(StudentRecordCreateInfo.class);

        studentRecordFacade.createStudentRecord(memberId, type, semester, info);

        verify(studentRecordService).createStudentRecord(memberId, type, semester, info);
    }

    @Test
    @DisplayName("생활기록부 전체 수정이 정상 동작한다.")
    void updateStudentRecordOverview() {
        long memberId = 200L;
        long recordId = 2L;
        String studentNumber = "20204567";
        String studentName = "김철수";
        String description = "리더십을 발휘하여 팀 프로젝트를 성공적으로 완수함.";
        int byteCount = 120;

        willDoNothing().given(studentRecordService)
                .updateStudentRecordOverview(memberId, recordId, studentNumber, studentName, description, byteCount);

        studentRecordFacade.updateStudentRecordOverview(memberId, recordId, studentNumber, studentName, description,
                byteCount);

        verify(studentRecordService)
                .updateStudentRecordOverview(memberId, recordId, studentNumber, studentName, description, byteCount);
    }

    @Test
    @DisplayName("생활기록부 삭제가 정상 동작한다.")
    void deleteStudentRecordFacade() {
        long memberId = 300L;
        long recordId = 3L;

        willDoNothing().given(studentRecordService).deleteStudentRecord(memberId, recordId);

        studentRecordFacade.deleteStudentRecord(memberId, recordId);

        verify(studentRecordService).deleteStudentRecord(memberId, recordId);
    }
}

