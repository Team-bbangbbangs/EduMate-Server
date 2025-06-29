package com.edumate.eduserver.member.exception;

import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(final MemberErrorCode errorCode) {
        super(errorCode);
    }
}
