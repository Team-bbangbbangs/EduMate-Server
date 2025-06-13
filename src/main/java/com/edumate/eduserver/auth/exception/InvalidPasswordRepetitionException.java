package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;

public class InvalidPasswordRepetitionException extends BadRequestException {

    public InvalidPasswordRepetitionException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
