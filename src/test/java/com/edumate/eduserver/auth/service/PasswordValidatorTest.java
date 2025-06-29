package com.edumate.eduserver.auth.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.edumate.eduserver.auth.exception.InvalidPasswordFormatException;
import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    @Test
    @DisplayName("정상적인 비밀번호는 예외가 발생하지 않는다.")
    void validPassword() {
        assertThatCode(() -> PasswordValidator.validatePasswordFormat("Abcdef12!"))
                .doesNotThrowAnyException();
        assertThatCode(() -> PasswordValidator.validatePasswordFormat("1234abcd@"))
                .doesNotThrowAnyException();
        assertThatCode(() -> PasswordValidator.validatePasswordFormat("a1!b2@c3#"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("비밀번호가 8자 미만 또는 16자 초과면 InvalidPasswordLengthException이 발생한다.")
    void invalidPasswordLength() {
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("Abc12!"))
                .isInstanceOf(InvalidPasswordLengthException.class);
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("a1!b2@c3#d4$e5%f6^g7&"))
                .isInstanceOf(InvalidPasswordLengthException.class);
    }

    @Test
    @DisplayName("비밀번호가 영문, 숫자, 특수문자 중 2종류 이상을 포함하지 않으면 InvalidPasswordFormatException이 발생한다.")
    void invalidPasswordFormat() {
        // 영문만
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("abcdefgh"))
                .isInstanceOf(InvalidPasswordFormatException.class);
        // 숫자만
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("12345678"))
                .isInstanceOf(InvalidPasswordFormatException.class);
        // 특수문자만
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("!@#$%^&*"))
                .isInstanceOf(InvalidPasswordFormatException.class);
    }

    @Test
    @DisplayName("같은 문자가 3번 이상 연속되면 InvalidPasswordFormatException이 발생한다.")
    void invalidPasswordRepetition() {
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("aaa12345"))
                .isInstanceOf(InvalidPasswordFormatException.class);
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("11!111abc"))
                .isInstanceOf(InvalidPasswordFormatException.class);
        assertThatThrownBy(() -> PasswordValidator.validatePasswordFormat("abc!!!123"))
                .isInstanceOf(InvalidPasswordFormatException.class);
    }
}

