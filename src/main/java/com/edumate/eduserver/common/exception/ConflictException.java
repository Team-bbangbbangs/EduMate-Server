package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class ConflictException extends EduMateCustomException{

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;
    private final String code;

    public ConflictException(final ErrorCode errorCode) {
        super(errorCode, STATUS);
        this.code = errorCode.getCode();
    }
}
