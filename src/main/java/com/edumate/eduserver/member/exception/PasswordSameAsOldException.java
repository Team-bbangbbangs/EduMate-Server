package com.edumate.eduserver.member.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;

public class PasswordSameAsOldException extends BadRequestException {

    public PasswordSameAsOldException(final MemberErrorCode errorCode) {
        super(errorCode);
    }
}
