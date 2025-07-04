package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(final ErrorCode errorCode) {
        super(errorCode, STATUS);
    }

    public BadRequestException(final ErrorCode errorCode, final String customMessage) {
        super(errorCode, STATUS, customMessage);
    }
}
