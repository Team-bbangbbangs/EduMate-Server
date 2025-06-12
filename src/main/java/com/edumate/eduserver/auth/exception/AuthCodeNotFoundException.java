package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.NotFoundException;

public class AuthCodeNotFoundException extends NotFoundException {

    public AuthCodeNotFoundException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
