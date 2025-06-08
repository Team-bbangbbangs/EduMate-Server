package com.edumate.eduserver.notice.exception;

import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;

public class NoticeNotFoundException extends NotFoundException {

    public NoticeNotFoundException(final NoticeErrorCode errorCode) {
        super(errorCode);
    }
}
