package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;

public class UnMatchedCodeException extends BadRequestException {

    public UnMatchedCodeException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
