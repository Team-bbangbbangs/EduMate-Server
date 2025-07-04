package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.exception.InvalidPasswordFormatException;
import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 16;
    private static final Pattern VALID_PASSWORD_PATTERN = Pattern.compile("^(?!.*(.)\\1{2})" + // 같은 문자 3번 이상 연속 불가
            "(" + "(?=.*[A-Za-z])(?=.*\\d)" + // 영문 + 숫자
            "|" + "(?=.*[A-Za-z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~])" + // 영문 + 특수문자
            "|" + "(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~])" + // 숫자 + 특수문자
            ")" + "[A-Za-z\\d!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~]*$" // 허용 문자만
    );

    public static void validatePasswordFormat(final String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new InvalidPasswordLengthException(AuthErrorCode.INVALID_PASSWORD_LENGTH);
        }

        if (!VALID_PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordFormatException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }
}
