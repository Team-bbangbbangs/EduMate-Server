package com.edumate.eduserver.external.ai.exception;

import com.edumate.eduserver.common.exception.TooManyRequestException;
import com.edumate.eduserver.external.ai.exception.code.OpenAiErrorCode;

public class OpenAiQuotaExceededException extends TooManyRequestException {

    public OpenAiQuotaExceededException(final OpenAiErrorCode errorCode) {
        super(errorCode);
    }
}
