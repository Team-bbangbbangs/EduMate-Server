package com.edumate.eduserver.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(final String message) {
        super(message, STATUS);
    }
}
