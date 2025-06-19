package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class TooManyRequestException extends EduMateCustomException{

    private static final HttpStatus STATUS = HttpStatus.TOO_MANY_REQUESTS;

    public TooManyRequestException(final ErrorCode errorCode) {
        super(errorCode, STATUS);
    }
}
