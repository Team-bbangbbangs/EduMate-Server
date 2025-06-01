package com.edumate.eduserver.common.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EduMateCustomException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;

    public EduMateCustomException(final ErrorCode errorCode, final HttpStatus status) {
        super(errorCode.getMessage());
        this.status = status;
        this.errorCode = errorCode;
    }
}
