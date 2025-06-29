package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class StudentRecordDetailNotFoundException extends NotFoundException {

    public StudentRecordDetailNotFoundException(final StudentRecordErrorCode errorCode) {
        super(errorCode);
    }
}
