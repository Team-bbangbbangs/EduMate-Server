package com.edumate.eduserver.auth.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.edumate.eduserver.auth.exception.InvalidPasswordFormatException;
import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.auth.security.jwt.JwtGenerator;
import com.edumate.eduserver.auth.security.jwt.TokenType;
import com.edumate.eduserver.auth.service.AuthService;
import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.auth.service.RandomCodeGenerator;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.service.SubjectService;
import com.edumate.eduserver.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthFacadeTest {
    @Mock
    EmailService emailService;
    @Mock
    AuthService authService;
    @Mock
    MemberService memberService;
    @Mock
    SubjectService subjectService;
    @Mock
    JwtGenerator jwtGenerator;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RandomCodeGenerator randomCodeGenerator;

    @InjectMocks
    AuthFacade authFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입이 정상적으로 동작한다.")
    void signUp() {
        String email = "test@email.com";
        String password = "Abc12345!";
        String subjectName = "수학";
        String school = "테스트고";
        Subject subject = mock(Subject.class);
        String encodedPassword = "encoded";
        String memberUuid = "uuid-123";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        given(subjectService.getSubjectByName(subjectName)).willReturn(subject);
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        willDoNothing().given(authService).checkAlreadyRegistered(email);
        given(memberService.saveMember(email, encodedPassword, subject, school)).willReturn(memberUuid);
        given(jwtGenerator.generateToken(memberUuid, TokenType.ACCESS)).willReturn(accessToken);
        given(jwtGenerator.generateToken(memberUuid, TokenType.REFRESH)).willReturn(refreshToken);

        MemberSignUpResponse response = authFacade.signUp(email, password, subjectName, school);

        assertThat(response.accessToken()).isEqualTo(accessToken);
        assertThat(response.refreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("비밀번호 길이 오류시 예외가 발생한다.")
    void invalidPasswordLength() {
        String email = "test@email.com";
        String password = "short";
        String subjectName = "수학";
        String school = "테스트고";
        given(subjectService.getSubjectByName(subjectName)).willReturn(mock(Subject.class));
        assertThatThrownBy(() -> authFacade.signUp(email, password, subjectName, school))
                .isInstanceOf(InvalidPasswordLengthException.class);

        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    @DisplayName("비밀번호 형식 오류시 예외가 발생한다.")
    void invalidPasswordFormat() {
        String email = "test@email.com";
        String password = "abcdefgh";
        String subjectName = "수학";
        String school = "테스트고";
        given(subjectService.getSubjectByName(subjectName)).willReturn(mock(Subject.class));
        doThrow(new InvalidPasswordFormatException(AuthErrorCode.INVALID_PASSWORD_FORMAT))
                .when(passwordEncoder).encode(password);
        assertThatThrownBy(() -> authFacade.signUp(email, password, subjectName, school))
                .isInstanceOf(InvalidPasswordFormatException.class);
    }

    @Test
    @DisplayName("비밀번호 반복문자 오류시 예외가 발생한다.")
    void invalidPasswordRepetition() {
        String email = "test@email.com";
        String password = "aaa12345";
        String subjectName = "수학";
        String school = "테스트고";
        given(subjectService.getSubjectByName(subjectName)).willReturn(mock(Subject.class));
        doThrow(new InvalidPasswordFormatException(AuthErrorCode.INVALID_PASSWORD_FORMAT))
                .when(passwordEncoder).encode(password);
        assertThatThrownBy(() -> authFacade.signUp(email, password, subjectName, school))
                .isInstanceOf(InvalidPasswordFormatException.class);
    }
}

