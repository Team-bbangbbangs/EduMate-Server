package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    private final String code;

    public ForbiddenException(final ErrorCode errorCode) {
        super(errorCode, STATUS);
        this.code = errorCode.getCode();
    }
}
