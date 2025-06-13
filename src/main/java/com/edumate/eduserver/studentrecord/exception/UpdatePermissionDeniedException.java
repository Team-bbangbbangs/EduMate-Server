package com.edumate.eduserver.studentrecord.exception;

import com.edumate.eduserver.common.exception.ForbiddenException;
import com.edumate.eduserver.studentrecord.exception.code.StudentRecordErrorCode;

public class UpdatePermissionDeniedException extends ForbiddenException {

    public UpdatePermissionDeniedException(final StudentRecordErrorCode errorCode) {
        super(errorCode);
    }
}
