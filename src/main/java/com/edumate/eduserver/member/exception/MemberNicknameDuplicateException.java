package com.edumate.eduserver.member.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;

public class MemberNicknameDuplicateException extends BadRequestException {

    public MemberNicknameDuplicateException(final MemberErrorCode errorCode, final String input) {
        super(errorCode, String.format(errorCode.getMessage(), input));
    }
}
