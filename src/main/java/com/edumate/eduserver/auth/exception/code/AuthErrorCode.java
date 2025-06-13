package com.edumate.eduserver.auth.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    // 400 Bad Request
    ILLEGAL_URL_ARGUMENT("EDMT-4000101", "잘못된 URL 인자입니다."),
    EXPIRED_CODE("EDMT-4000102", "이미 만료된 인증 코드입니다."),
    INVALID_CODE("EDMT-4000103", "유효하지 않은 인증 코드입니다."),

    // 401 Unauthorized,
    INVALID_TOKEN_VALUE("EDMT-4010101", "유효하지 않은 %s 토큰입니다."),
    EXPIRED_TOKEN("EDMT-4010102", "이미 만료된 %s 토큰입니다."),
    UNAUTHORIZED("EDMT-4010103", "인증되지 않은 사용자입니다."),
    MISSED_TOKEN("EDMT-4010104", "인증 토큰이 누락되었습니다."),

    // 404 Not Found
    AUTH_CODE_NOT_FOUND("EDMT-4040101", "유효한 인증 코드가 존재하지 않습니다.");

    private final String code;
    private final String message;
}
