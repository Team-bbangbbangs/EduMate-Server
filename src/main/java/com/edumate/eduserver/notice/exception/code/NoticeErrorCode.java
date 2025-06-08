package com.edumate.eduserver.notice.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode implements ErrorCode {
    INVALID_NOTICE_CATEGORY("EDMT-4000301", "유효하지 않은 공지사항 카테고리입니다.")
    ;

    private final String code;
    private final String message;
}
