package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;

public class PasswordSameAsOldException extends BadRequestException {

    public PasswordSameAsOldException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
