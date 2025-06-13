package com.edumate.eduserver.auth.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    // 400 Bad Request
    ILLEGAL_URL_ARGUMENT("EDMT-4000106", "잘못된 URL 인자입니다."),
    EXPIRED_CODE("EDMT-4000108", "이미 만료된 인증 코드입니다."),
    INVALID_CODE("EDMT-4000109", "유효하지 않은 인증 코드입니다."),
    MISSED_TOKEN("EDMT-4000111", "인증 토큰이 누락되었습니다."),

    // 401 Unauthorized,
    INVALID_ACCESS_TOKEN_VALUE("EDMT-4010101", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_ACCESS_TOKEN("EDMT-4010102", "이미 만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("EDMT-4010103", "이미 만료된 리프레시 토큰입니다."),
    INVALID_REFRESH_TOKEN_VALUE("EDMT-4010104", "유효하지 않은 리프레시 토큰입니다."),
    UNAUTHORIZED("EDMT-4010110", "인증되지 않은 사용자입니다."),

    // 404 Not Found
    AUTH_CODE_NOT_FOUND("EDMT-4040107", "유효한 인증 코드가 존재하지 않습니다.");

    private final String code;
    private final String message;
}
