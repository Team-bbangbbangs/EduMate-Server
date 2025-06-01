package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public ForbiddenException(final ErrorCode errorCode) {
        super(errorCode, STATUS);
    }
}
