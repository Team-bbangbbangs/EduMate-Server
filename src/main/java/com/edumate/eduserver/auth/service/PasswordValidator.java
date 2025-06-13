package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.exception.InvalidPasswordFormatException;
import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import com.edumate.eduserver.auth.exception.InvalidPasswordRepetitionException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import java.util.regex.Pattern;

public class PasswordValidator {

    private static final Pattern LENGTH_PATTERN = Pattern.compile("^.{8,16}$");
    private static final Pattern COMBINATION_PATTERN = Pattern.compile(
            "^(?:(?=.*[A-Za-z])(?=.*\\d)|(?=.*[A-Za-z])(?=.*[^A-Za-z\\d])|(?=.*\\d)(?=.*[^A-Za-z\\d])).*$"
    );
    private static final Pattern REPEATED_CHAR_PATTERN = Pattern.compile("(.)\\1\\1");

    public static void validate(String password) {
        if (!LENGTH_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordLengthException(AuthErrorCode.INVALID_PASSWORD_LENGTH);
        }

        if (!COMBINATION_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordFormatException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }

        if (REPEATED_CHAR_PATTERN.matcher(password).find()) {
            throw new InvalidPasswordRepetitionException(AuthErrorCode.INVALID_PASSWORD_REPETITION);
        }
    }
}
