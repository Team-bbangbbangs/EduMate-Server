package com.edumate.eduserver.notice.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;

public class NoticeCategoryNotFoundException extends BadRequestException {

    public NoticeCategoryNotFoundException(final NoticeErrorCode errorCode) {
        super(errorCode);
    }
}
