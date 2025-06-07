package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class RecordTypeNotFoundException extends BadRequestException {

    public RecordTypeNotFoundException(final StudentRecordErrorCode errorCode, final String recordType) {
        super(errorCode, errorCode.formatMessage(recordType));
    }
}
