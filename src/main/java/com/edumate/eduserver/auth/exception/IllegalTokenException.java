package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.UnauthorizedException;

public class IllegalTokenException extends UnauthorizedException {

    public IllegalTokenException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
