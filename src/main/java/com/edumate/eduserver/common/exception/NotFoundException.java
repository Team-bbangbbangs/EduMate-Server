package com.edumate.eduserver.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends EduMateCustomException {

    private static final String MESSAGE = "존재하지 않는 %s 입니다.";
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException(final String target) {
        super(String.format(MESSAGE, target), STATUS);
    }
}
