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
    ALREADY_USED_CODE("EDMT-4000109", "이미 사용된 인증 코드입니다. 새로운 코드로 다시 인증을 진행해주세요."),

    // 401 Unauthorized
    INVALID_ACCESS_TOKEN_VALUE("EDMT-4010101", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_ACCESS_TOKEN("EDMT-4010102", "이미 만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("EDMT-4010103", "이미 만료된 리프레시 토큰입니다."),
    INVALID_REFRESH_TOKEN_VALUE("EDMT-4010104", "유효하지 않은 리프레시 토큰입니다."),

    // 404 Not Found
    AUTH_CODE_NOT_FOUND("EDMT-4040107", "인증 코드가 존재하지 않습니다.");

    private final String code;
    private final String message;
}
