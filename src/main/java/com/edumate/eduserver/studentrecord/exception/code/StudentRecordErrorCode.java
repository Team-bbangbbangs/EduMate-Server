package com.edumate.eduserver.studentrecord.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentRecordErrorCode implements ErrorCode {

    RECORD_TYPE_NOT_FOUND("EDMT-40001", "유효하지 않는 생활기록부 항목입니다."),
    MEMBER_STUDENT_RECORD_NOT_FOUND("EDMT-40401", "해당 회원의 해당 학기 생활기록부가 존재하지 않습니다."),
    STUDENT_RECORD_DETAIL_NOT_FOUND("EDMT-40402", "해당 학생에 대한 생활기록부 기록이 존재하지 않습니다."),;

    private final String code;
    private final String message;
}
