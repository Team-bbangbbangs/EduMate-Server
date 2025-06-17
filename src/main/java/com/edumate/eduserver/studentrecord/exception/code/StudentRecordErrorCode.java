package com.edumate.eduserver.studentrecord.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentRecordErrorCode implements ErrorCode {

    // 400 Bad Request
    RECORD_TYPE_NOT_FOUND("EDMT-4000201", "입력하신 %s는 유효하지 않는 생활기록부 항목입니다."),
    INVALID_SEMESTER_FORMAT("EDMT-4000202", "입력하신 %s는 유효하지 않은 학기 형식입니다. 올바른 형식은 'YYYY-1' 또는 'YYYY-2'입니다."),

    // 403 Forbidden
    UPDATE_PERMISSION_DENIED("EDMT-4030201", "회원님의 생활기록부 항목이 아닙니다. 해당 항목을 수정할 권한이 없습니다."),

    // 404 Not Found,
    STUDENT_RECORD_DETAIL_NOT_FOUND("EDMT-4040202", "해당 학생에 대한 생활기록부 기록이 존재하지 않습니다."),
    MEMBER_STUDENT_RECORD_NOT_FOUND("EDMT-4040201", "해당 회원의 해당 학기 생활기록부가 존재하지 않습니다."),

    // 409 Conflict
    RECORD_ALREADY_EXISTS("EDMT-4090201", "이미 해당 학기 생활기록부가 존재합니다. 중복된 기록을 추가할 수 없습니다."),;

    private final String code;
    private final String message;
}
