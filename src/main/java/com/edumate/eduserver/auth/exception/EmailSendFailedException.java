package com.edumate.eduserver.auth.exception;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import org.springframework.http.HttpStatus;

public class EmailSendFailedException extends EduMateCustomException {

    public EmailSendFailedException(final AuthErrorCode errorCode) {
        super(errorCode, HttpStatus.BAD_GATEWAY);
    }
}
