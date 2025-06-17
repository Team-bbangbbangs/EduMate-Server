package com.edumate.eduserver.member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.domain.Role;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.exception.MemberNotFoundException;
import com.edumate.eduserver.member.repository.MemberRepository;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.repository.SubjectRepository;
import com.edumate.eduserver.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("멤버 서비스 테스트")
class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    @DisplayName("ID로 회원 정보를 정상적으로 조회한다.")
    void getMemberById_success() {
        // given
        Subject subject = subjectRepository.save(Subject.create("체육"));
        Member savedMember = memberRepository.save(Member.create(
                subject, "test@example.com", "encodedPw", "닉네임", School.MIDDLE_SCHOOL, Role.PENDING_TEACHER
        ));
        long memberId = savedMember.getId();

        // when
        Member result = memberService.getMemberById(memberId);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(memberId),
                () -> assertThat(result.getEmail()).isEqualTo("test@example.com"),
                () -> assertThat(result.getRole()).isEqualTo(Role.PENDING_TEACHER),
                () -> assertThat(result.getSchool()).isEqualTo(School.MIDDLE_SCHOOL),
                () -> assertThat(subject.getName()).isEqualTo("체육")
        );
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID 조회 시 예외가 발생한다.")
    void getMemberById_failure() {
        // given
        long invalidId = 999L;

        // when & then
        assertThatThrownBy(() -> memberService.getMemberById(invalidId))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
