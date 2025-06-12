package com.edumate.eduserver.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.edumate.eduserver.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RandomCodeGeneratorTest extends ServiceTest {

    @Autowired
    private RandomCodeGenerator randomCodeGenerator;

    @Test
    @DisplayName("인증코드 길이가 정상적으로 생성되는지 확인한다.")
    void generateCode_ShouldHaveCorrectFormat() {
        // given
        String generatedCode = randomCodeGenerator.generate();

        // then
        assertThat(generatedCode).hasSize(6);
    }
}
