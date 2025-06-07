package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.code.ErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class RecordTypeNotFoundException extends BadRequestException {

    public RecordTypeNotFoundException(final ErrorCode errorCode) {
        super(StudentRecordErrorCode.RECORD_TYPE_NOT_FOUND);
    }
}
