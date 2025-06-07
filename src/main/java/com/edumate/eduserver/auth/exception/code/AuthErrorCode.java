package com.edumate.eduserver.auth.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    // 401 Unauthorized
    INVALID_ACCESS_TOKEN_VALUE("EDMT-4010101", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_ACCESS_TOKEN("EDMT-4010102", "이미 만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("EDMT-4010103", "이미 만료된 리프레시 토큰입니다."),
    INVALID_REFRESH_TOKEN_VALUE("EDMT-4010104", "유효하지 않은 리프레시 토큰입니다.");

    private final String code;
    private final String message;
}
