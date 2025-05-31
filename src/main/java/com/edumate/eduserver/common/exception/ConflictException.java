package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class ConflictException extends EduMateCustomException{

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public ConflictException(final ErrorCode errorCode) {
        super(errorCode.getMessage(), STATUS, errorCode.getCode());
    }
}
