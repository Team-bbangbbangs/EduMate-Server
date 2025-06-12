package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;

public class ExpiredCodeException extends BadRequestException {

    public ExpiredCodeException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
