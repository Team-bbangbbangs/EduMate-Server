package com.edumate.eduserver.external.ai.exception;

import com.edumate.eduserver.common.exception.TooManyRequestException;
import com.edumate.eduserver.external.ai.exception.code.OpenAiErrorCode;

public class OpenAiRateLimitExceededException extends TooManyRequestException {

    public OpenAiRateLimitExceededException(final OpenAiErrorCode errorCode) {
        super(errorCode);
    }
}
