package com.edumate.eduserver.studentrecord.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentRecordErrorCode implements ErrorCode {

    RECORD_TYPE_NOT_FOUND("EDMT-40001", "유효하지 않는 생활기록부 항목입니다.");

    private final String code;
    private final String message;
}
