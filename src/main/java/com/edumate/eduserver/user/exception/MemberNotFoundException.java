package com.edumate.eduserver.user.exception;

import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.user.exception.code.MemberErrorCode;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(final MemberErrorCode errorCode) {
        super(errorCode);
    }
}
