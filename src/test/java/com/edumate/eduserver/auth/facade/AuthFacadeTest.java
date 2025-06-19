package com.edumate.eduserver.auth.facade;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.edumate.eduserver.auth.exception.InvalidPasswordFormatException;
import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.service.AuthService;
import com.edumate.eduserver.auth.service.RandomCodeGenerator;
import com.edumate.eduserver.auth.service.TokenService;
import com.edumate.eduserver.external.aws.mail.EmailService;
import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.service.SubjectService;
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
    TokenService tokenService;
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

