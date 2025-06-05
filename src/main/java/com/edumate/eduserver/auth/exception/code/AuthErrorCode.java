package com.edumate.eduserver.auth.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_ACCESS_TOKEN_VALUE("EDMT-40401", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_ACCESS_TOKEN("EDMT-40402", "이미 만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("EDMT-40403", "이미 만료된 리프레시 토큰입니다."),
    INVALID_REFRESH_TOKEN_VALUE("EDMT-40404", "유효하지 않은 리프레시 토큰입니다.");

    private final String code;
    private final String message;
}
