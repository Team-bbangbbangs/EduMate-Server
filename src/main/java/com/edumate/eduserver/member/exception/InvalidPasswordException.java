package com.edumate.eduserver.member.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;

public class InvalidPasswordException extends BadRequestException {

    public InvalidPasswordException(final MemberErrorCode errorCode) {
        super(errorCode);
    }
}
