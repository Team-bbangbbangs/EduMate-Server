package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(final ErrorCode errorCode) {
        super(errorCode, STATUS);
    }
}
