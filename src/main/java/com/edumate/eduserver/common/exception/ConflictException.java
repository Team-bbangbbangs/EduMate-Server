package com.edumate.eduserver.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends EduMateCustomException{

    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public ConflictException(final String message) {
        super(message, STATUS);
    }
}
