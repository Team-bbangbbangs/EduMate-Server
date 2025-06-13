package com.edumate.eduserver.studentrecord.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
<<<<<<< HEAD
<<<<<<< HEAD
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
=======
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;
>>>>>>> e11cd12 ([test] facade 클래스에 대한 테스트 코드 작성)
=======
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
>>>>>>> 956ca9e ([test] 권한 오류에 대한 테스트 코드 작성)

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> e11cd12 ([test] facade 클래스에 대한 테스트 코드 작성)
=======
>>>>>>> 956ca9e ([test] 권한 오류에 대한 테스트 코드 작성)
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.service.MemberService;
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
<<<<<<< HEAD
<<<<<<< HEAD
        Member member = mock(Member.class);
=======
         Member member = mock(Member.class);
>>>>>>> e11cd12 ([test] facade 클래스에 대한 테스트 코드 작성)
=======
        Member member = mock(Member.class);
>>>>>>> 956ca9e ([test] 권한 오류에 대한 테스트 코드 작성)
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
}

