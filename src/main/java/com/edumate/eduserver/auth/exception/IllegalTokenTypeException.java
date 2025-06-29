package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.UnauthorizedException;

public class IllegalTokenTypeException extends UnauthorizedException {

    public IllegalTokenTypeException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
