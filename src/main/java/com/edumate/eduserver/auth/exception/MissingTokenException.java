package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.UnauthorizedException;

public class MissingTokenException extends UnauthorizedException {

    public MissingTokenException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
