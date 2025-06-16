package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.UnauthorizedException;

public class MismatchedPasswordException extends UnauthorizedException {

    public MismatchedPasswordException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
