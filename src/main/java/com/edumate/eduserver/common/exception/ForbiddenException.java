package com.edumate.eduserver.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends EduMateCustomException {

    private static final String MESSAGE = "접근 권한이 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public ForbiddenException() {
        super(MESSAGE, STATUS);
    }
}
