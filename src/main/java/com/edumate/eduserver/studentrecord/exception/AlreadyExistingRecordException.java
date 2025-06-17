package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.exception.ConflictException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class AlreadyExistingRecordException extends ConflictException {

    public AlreadyExistingRecordException(final StudentRecordErrorCode errorCode) {
        super(errorCode);
    }
}
