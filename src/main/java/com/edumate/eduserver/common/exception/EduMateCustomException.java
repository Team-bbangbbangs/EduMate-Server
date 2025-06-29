package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class EduMateCustomException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final String customMessage;

    public EduMateCustomException(final ErrorCode errorCode, final HttpStatus status) {
        this(errorCode, status, errorCode.getMessage());
    }

    public EduMateCustomException(final ErrorCode errorCode, final HttpStatus status, final String customMessage) {
        super(customMessage);
        this.status = status;
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }
}
