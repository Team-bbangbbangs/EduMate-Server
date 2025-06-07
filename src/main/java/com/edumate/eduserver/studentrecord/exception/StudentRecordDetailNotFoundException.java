package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class StudentRecordDetailNotFoundException extends BadRequestException {

    public StudentRecordDetailNotFoundException(final StudentRecordErrorCode errorCode) {
        super(errorCode);
    }
}
