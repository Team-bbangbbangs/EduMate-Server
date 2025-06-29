package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.UnauthorizedException;

public class InvalidSignatureTokenException extends UnauthorizedException {

    public InvalidSignatureTokenException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
