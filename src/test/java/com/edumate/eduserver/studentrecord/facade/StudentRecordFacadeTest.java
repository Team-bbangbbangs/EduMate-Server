package com.edumate.eduserver.studentrecord.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.edumate.eduserver.member.domain.Member;
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
        String memberUuid = "uuid-1";
        long recordId = 1L;
        String description = "안녕하세요";
        int byteCount = 10;
        Member member = mock(Member.class);
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(100L);
        willDoNothing().given(studentRecordService).update(100L, recordId, description, byteCount);
        studentRecordFacade.updateStudentRecord(memberUuid, recordId, description, byteCount);
        verify(studentRecordService).update(100L, recordId, description, byteCount);
    }

    @Test
    @DisplayName("학생 기록 상세 조회가 정상 동작한다.")
    void getStudentRecord() {
        String memberUuid = "uuid-2";
        long recordId = 2L;
        Member member = mock(Member.class);
        StudentRecordDetail detail = mock(StudentRecordDetail.class);
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(200L);
        given(studentRecordService.getRecordDetail(200L, recordId)).willReturn(detail);
        given(detail.getDescription()).willReturn("desc");
        given(detail.getByteCount()).willReturn(20);
        StudentRecordDetailResponse response = studentRecordFacade.getStudentRecord(memberUuid, recordId);
        assertThat(response.description()).isEqualTo("desc");
        assertThat(response.byteCount()).isEqualTo(20);
    }

    @Test
    @DisplayName("학생 기록 오버뷰 조회가 정상 동작한다.")
    void getStudentRecordOverviews() {
        String memberUuid = "uuid-3";
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-1";
        Member member = mock(Member.class);
        List<StudentRecordDetail> details = List.of(mock(StudentRecordDetail.class));
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(300L);
        given(studentRecordService.getAll(300L, type, semester)).willReturn(details);
        StudentRecordOverviewsResponse response = studentRecordFacade.getStudentRecordOverviews(memberUuid, type,
                semester);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("학생 이름 목록 조회가 정상 동작한다.")
    void getStudentDetails() {
        String memberUuid = "uuid-4";
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-2";
        Member member = mock(Member.class);
        List<StudentRecordDetail> details = List.of(mock(StudentRecordDetail.class));
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(400L);
        given(studentRecordService.getStudentNames(400L, type, semester)).willReturn(details);
        StudentNamesResponse response = studentRecordFacade.getStudentDetails(memberUuid, type, semester);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("학생 기록 생성이 정상 동작한다.")
    void createStudentRecords() {
        String memberUuid = "uuid-5";
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-2";
        List<StudentRecordInfo> infos = List.of(mock(StudentRecordInfo.class));
        Member member = mock(Member.class);
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(500L);
        willDoNothing().given(studentRecordService).createStudentRecords(500L, type, semester, infos);
        studentRecordFacade.createStudentRecords(memberUuid, type, semester, infos);
        verify(studentRecordService).createStudentRecords(500L, type, semester, infos);
    }

    @Test
    @DisplayName("학생 기록 생성이 정상 동작한다.")
    void createStudentRecord() {
        String memberUuid = "uuid-5";
        StudentRecordType type = StudentRecordType.ABILITY_DETAIL;
        String semester = "2024-2";
        StudentRecordCreateInfo info = mock(StudentRecordCreateInfo.class);
        Member member = mock(Member.class);
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(500L);
        studentRecordFacade.createStudentRecord(memberUuid, type, semester, info);
        verify(studentRecordService).createStudentRecord(500L, type, semester, info);
    }

    @Test
    @DisplayName("생활기록부 전체 수정이 정상 동작한다.")
    void updateStudentRecordOverview() {
        String memberUuid = "uuid-2";
        long recordId = 2L;
        String studentNumber = "20204567";
        String studentName = "김철수";
        String description = "리더십을 발휘하여 팀 프로젝트를 성공적으로 완수함.";
        int byteCount = 120;
        Member member = mock(Member.class);
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(200L);
        willDoNothing().given(studentRecordService)
                .updateStudentRecordOverview(200L, recordId, studentNumber, studentName, description, byteCount);
        studentRecordFacade.updateStudentRecordOverview(memberUuid, recordId, studentNumber, studentName, description, byteCount);
        verify(studentRecordService)
                .updateStudentRecordOverview(200L, recordId, studentNumber, studentName, description, byteCount);
    }

    @Test
    @DisplayName("생활기록부 삭제가 정상 동작한다.")
    void deleteStudentRecordFacade() {
        String memberUuid = "uuid-3";
        long recordId = 3L;
        Member member = mock(Member.class);
        given(memberService.getMemberByUuid(memberUuid)).willReturn(member);
        given(member.getId()).willReturn(300L);
        willDoNothing().given(studentRecordService).deleteStudentRecord(300L, recordId);
        studentRecordFacade.deleteStudentRecord(memberUuid, recordId);
        verify(studentRecordService).deleteStudentRecord(300L, recordId);
    }
}

