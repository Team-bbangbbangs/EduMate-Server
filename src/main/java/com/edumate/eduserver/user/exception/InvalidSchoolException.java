package com.edumate.eduserver.user.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.user.exception.code.MemberErrorCode;

public class InvalidSchoolException extends BadRequestException {

    public InvalidSchoolException(final MemberErrorCode errorCode, final String input) {
        super(errorCode, String.format(errorCode.getMessage(), input));
    }
}
