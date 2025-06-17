package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.UnauthorizedException;

public class MismatchedTokenException extends UnauthorizedException {

    public MismatchedTokenException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
