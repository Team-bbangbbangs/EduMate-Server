package com.edumate.eduserver.common.exception;

import org.springframework.http.HttpStatus;

public class EduMateCustomException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public EduMateCustomException(final String message, final HttpStatus status, final String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public String getStatusCode() {
        return String.valueOf(status.value());
    }

    public String getErrorCode() {
        return errorCode;
    }
}
