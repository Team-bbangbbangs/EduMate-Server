package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class InvalidSemesterFormatException extends BadRequestException {

    public InvalidSemesterFormatException(final StudentRecordErrorCode errorCode, final String semester) {
        super(errorCode, errorCode.formatMessage(semester));
    }
}
