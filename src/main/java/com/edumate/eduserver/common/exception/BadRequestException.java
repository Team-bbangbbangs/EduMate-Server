package com.edumate.eduserver.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends EduMateCustomException{

    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(final String message) {
        super(message, STATUS);
    }
}
