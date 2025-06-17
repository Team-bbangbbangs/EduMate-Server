package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.exception.InvalidPasswordFormatException;
import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static final Pattern LENGTH_PATTERN = Pattern.compile("^.{8,16}$");
    private static final Pattern VALID_PASSWORD_PATTERN = Pattern.compile(
            "^(?!.*(.)\\1{2})" + // 같은 문자 3번 이상 연속 불가
                    "(?=.{8,16}$)" +     // 길이: 8~16자
                    "(" +
                    "(?=.*[A-Za-z])(?=.*\\d)" + // 영문 + 숫자
                    "|" +
                    "(?=.*[A-Za-z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~])" + // 영문 + 특수문자
                    "|" +
                    "(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~])" + // 숫자 + 특수문자
                    ")" +
                    "[A-Za-z\\d!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~]*$" // 허용 문자만
    );

    public static void validate(final String password) {
        if (!LENGTH_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordLengthException(AuthErrorCode.INVALID_PASSWORD_LENGTH);
        }

        if (!VALID_PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordFormatException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }
}
