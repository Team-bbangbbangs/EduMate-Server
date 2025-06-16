package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;

public class MismatchedPasswordException extends BadRequestException {

    public MismatchedPasswordException(final MemberErrorCode errorCode) {
        super(errorCode);
    }
}
