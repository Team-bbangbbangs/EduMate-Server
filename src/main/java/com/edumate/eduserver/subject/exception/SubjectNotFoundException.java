package com.edumate.eduserver.subject.exception;

import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.subject.exception.code.SubjectErrorCode;

public class SubjectNotFoundException extends NotFoundException {

    public SubjectNotFoundException(final SubjectErrorCode errorCode) {
        super(errorCode);
    }
}
