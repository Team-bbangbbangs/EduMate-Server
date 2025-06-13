package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;

public class MissingTokenException extends BadRequestException {

    public MissingTokenException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
