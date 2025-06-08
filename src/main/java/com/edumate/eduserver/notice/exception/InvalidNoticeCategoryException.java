package com.edumate.eduserver.notice.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;

public class InvalidNoticeCategoryException extends BadRequestException {

    public InvalidNoticeCategoryException(final NoticeErrorCode errorCode) {
        super(errorCode);
    }
}
