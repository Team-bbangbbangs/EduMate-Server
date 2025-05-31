package com.edumate.eduserver.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EduMateCustomException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public EduMateCustomException(final String message, final HttpStatus status, final String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
