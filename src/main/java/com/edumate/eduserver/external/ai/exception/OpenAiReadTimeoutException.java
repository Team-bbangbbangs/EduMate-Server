package com.edumate.eduserver.external.ai.exception;

import com.edumate.eduserver.common.exception.EduMateCustomException;
import com.edumate.eduserver.external.ai.exception.code.OpenAiErrorCode;
import org.springframework.http.HttpStatus;

public class OpenAiReadTimeoutException extends EduMateCustomException {

    private static final HttpStatus STATUS = HttpStatus.REQUEST_TIMEOUT;

    public OpenAiReadTimeoutException(final OpenAiErrorCode errorCode) {
        super(errorCode, STATUS, errorCode.getMessage());
    }
} 