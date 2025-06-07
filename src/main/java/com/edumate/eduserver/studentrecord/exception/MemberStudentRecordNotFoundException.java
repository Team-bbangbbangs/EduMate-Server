package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class MemberStudentRecordNotFoundException extends NotFoundException {

    public MemberStudentRecordNotFoundException(final StudentRecordErrorCode errorCode) {
        super(errorCode);
    }
}
