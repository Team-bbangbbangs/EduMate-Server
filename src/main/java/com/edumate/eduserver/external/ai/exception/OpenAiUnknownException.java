package com.edumate.eduserver.external.ai.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import org.springframework.http.HttpStatus;

public class OpenAiUnknownException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public OpenAiUnknownException(final ErrorCode errorCode) {
        super(errorCode, STATUS, errorCode.getMessage());
    }
}
