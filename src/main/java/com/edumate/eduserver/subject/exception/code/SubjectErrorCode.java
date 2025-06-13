package com.edumate.eduserver.subject.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubjectErrorCode implements ErrorCode {

    // 404 Not Found
    SUBJECT_NOT_FOUND("EDMT-4040501", "해당 과목이 존재하지 않습니다.");

    private final String code;
    private final String message;
}
