package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.ConflictException;

public class MemberAlreadyRegisteredException extends ConflictException {

    public MemberAlreadyRegisteredException(final AuthErrorCode errorCode) {
        super(errorCode);
    }
}
