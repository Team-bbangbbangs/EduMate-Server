package com.edumate.eduserver.notice.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements ErrorCode {

    // 400 Bad Request
    INVALID_NOTICE_CATEGORY("EDMT-4000301", "유효하지 않은 공지사항 카테고리입니다."),
    UNWRITABLE_NOTICE_CATEGORY("EDMT-4000302", "공지사항 작성이 허용되지 않는 카테고리입니다."),

    // 404 Not Found,
    NOTICE_NOT_FOUND("EDMT-4040301", "해당 공지사항이 존재하지 않습니다."),
    ;

    private final String code;
    private final String message;
}
