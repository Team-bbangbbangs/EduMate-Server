package com.edumate.eduserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorCode implements ErrorCode {

    BAD_REQUEST("EDMT-40000", "잘못된 요청입니다."),
    NOT_FOUND("EDMT-40400", "요청한 자원을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("EDMT-50000", "서버 내부 오류가 발생했습니다."),
    METHOD_NOT_ALLOWED("EDMT-40500", "허용되지 않은 HTTP 메소드입니다.");

    private final String code;
    private final String message;

}
