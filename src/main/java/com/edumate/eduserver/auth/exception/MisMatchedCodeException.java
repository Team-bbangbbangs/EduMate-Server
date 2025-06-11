package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;

public class MisMatchedCodeException extends BadRequestException {

    public MisMatchedCodeException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
