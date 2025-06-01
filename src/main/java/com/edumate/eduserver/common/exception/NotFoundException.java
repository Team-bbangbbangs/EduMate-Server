package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private final String code;

    public NotFoundException(final ErrorCode errorCode) {
        super(errorCode, STATUS);
        this.code = errorCode.getCode();
    }
}
