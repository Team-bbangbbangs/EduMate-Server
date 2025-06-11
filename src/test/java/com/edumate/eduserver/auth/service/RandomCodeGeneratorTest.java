package com.edumate.eduserver.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import com.edumate.eduserver.auth.domain.AuthorizeStatus;
import com.edumate.eduserver.auth.repository.AuthorizationCodeRepository;
import com.edumate.eduserver.util.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class RandomCodeGeneratorTest extends ServiceTest {

    @Autowired
    private RandomCodeGenerator randomCodeGenerator;

    @MockitoBean
    private AuthorizationCodeRepository authorizationCodeRepository;

    @Captor
    private ArgumentCaptor<AuthorizationCode> authCodeCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("인증코드 길이 및 형식이 정상적으로 생성되는지 확인한다.")
    void generateCode_ShouldHaveCorrectFormat() {
        // when
        String generatedCode = randomCodeGenerator.getCode(defaultTeacher);

        // then
        assertThat(generatedCode).hasSize(6);
        assertThat(generatedCode).matches("^\\d{6}$");
    }

    @Test
    @DisplayName("인증코드가 데이터베이스에 올바르게 저장되는지 확인한다")
    void generateCode_ShouldSaveToDatabase() {
        // when
        String generatedCode = randomCodeGenerator.getCode(defaultTeacher);

        // then
        verify(authorizationCodeRepository).save(authCodeCaptor.capture());
        AuthorizationCode savedCode = authCodeCaptor.getValue();

        assertThat(savedCode.getMember()).isEqualTo(defaultTeacher);
        assertThat(savedCode.getAuthorizationCode()).isEqualTo(generatedCode);
        assertThat(savedCode.getStatus()).isEqualTo(AuthorizeStatus.PENDING);
    }
}
