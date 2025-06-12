package com.edumate.eduserver.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import com.edumate.eduserver.auth.domain.AuthorizeStatus;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.repository.SubjectRepository;
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.domain.Role;
import com.edumate.eduserver.user.domain.School;
import com.edumate.eduserver.user.repository.MemberRepository;
import com.edumate.eduserver.util.RepositoryTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("인증 코드 레포지토리 테스트")
class AuthorizationCodeRepositoryTest extends RepositoryTest {

    @Autowired
    private AuthorizationCodeRepository authorizationCodeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        Subject subject = Subject.create("국어");
        subjectRepository.save(subject);
        member = memberRepository.save(Member.create(subject, "test@email", "testtesttesttest", "testNickname",
                School.HIGH_SCHOOL, Role.TEACHER));
    }

    @Test
    @DisplayName("memberId와 status 기준으로 가장 최근 ID의 AuthorizationCode를 조회한다")
    void findTop1ByMemberIdAndStatusOrderByIdDescTest() {
        // given
        AuthorizeStatus status = AuthorizeStatus.PENDING;

        AuthorizationCode code1 = AuthorizationCode.create(member, "code1", status);
        AuthorizationCode code2 = AuthorizationCode.create(member, "code2", status);
        AuthorizationCode code3 = AuthorizationCode.create(member, "code3", status);

        authorizationCodeRepository.save(code1);
        authorizationCodeRepository.save(code2);
        authorizationCodeRepository.save(code3);

        // when
        Optional<AuthorizationCode> result = authorizationCodeRepository
                .findTop1ByMemberIdAndStatusOrderByIdDesc(member.getId(), status);

        // then
        assertThat(result.get().getAuthorizationCode()).isEqualTo("code3");
    }
}
