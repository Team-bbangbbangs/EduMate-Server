package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.UnauthorizedException;

public class ExpiredTokenException extends UnauthorizedException {

    public ExpiredTokenException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}
