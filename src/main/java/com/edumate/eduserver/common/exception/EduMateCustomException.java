package com.edumate.eduserver.common.exception;

import org.springframework.http.HttpStatus;

public class EduMateCustomException extends RuntimeException {

    private final HttpStatus status;

    public EduMateCustomException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public String statusCode() {
        return String.valueOf(status.value());
    }
}
