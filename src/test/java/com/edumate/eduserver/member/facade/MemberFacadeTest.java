package com.edumate.eduserver.member.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.facade.response.MemberProfileGetResponse;
import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.subject.domain.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MemberFacadeTest {
    @Mock
    MemberService memberService;
    @InjectMocks
    MemberFacade memberFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("학생 기록 상세 조회가 정상 동작한다.")
    void getStudentRecord() {
        long memberId = 200L;
        Member member = mock(Member.class);
        Subject subject = mock(Subject.class);
        School school = mock(School.class);
        given(subject.getName()).willReturn("수학");
        given(school.getName()).willReturn(School.HIGH_SCHOOL.getName());
        given(member.getSubject()).willReturn(subject);
        given(member.getEmail()).willReturn("test@example.com");
        given(member.isVerifyTeacher()).willReturn(true);
        given(member.getSchool()).willReturn(school);
        given(memberService.getMemberById(memberId)).willReturn(member);

        // when
        MemberProfileGetResponse response = memberFacade.getMemberProfile(memberId);

        // then
        assertThat(response).isNotNull();
    }
}
