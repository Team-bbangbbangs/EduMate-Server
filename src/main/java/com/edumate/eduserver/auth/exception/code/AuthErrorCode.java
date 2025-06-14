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
    INVALID_PASSWORD_LENGTH("EDMT-4000104", "비밀번호는 8자 이상 16자 이하로 입력해주세요."),
    INVALID_PASSWORD_FORMAT("EDMT-4000105", "영문, 숫자, 특수문자 중 2종류 이상을 포함해야하며 같은 문자를 3번 이상 연속해서 사용할 수 없습니다."),
    MISMATCHED_PASSWORD("EDMT-4000106", "비밀번호가 일치하지 않습니다."),

    // 401 Unauthorized,
    INVALID_TOKEN_VALUE("EDMT-4010101", "유효하지 않은 엑세스 토큰입니다."),
    EXPIRED_TOKEN("EDMT-4010102", "이미 만료된 토큰입니다."),
    UNAUTHORIZED("EDMT-4010103", "인증되지 않은 사용자입니다."),
    MISSED_TOKEN("EDMT-4010104", "인증 토큰이 누락되었습니다."),
    INVALID_TOKEN_TYPE("EDMT-4010105", "잘못된 유형의 토큰입니다. 요청에는 엑세스 토큰이 필요합니다."),
    INVALID_SIGNATURE_TOKEN("EDMT-4010106", "토큰의 서명이 유효하지 않습니다."),

    // 403 Forbidden
    FORBIDDEN("EDMT-4030101", "권한이 부족한 사용자입니다."),

    // 404 Not Found,
    AUTH_CODE_NOT_FOUND("EDMT-4040101", "유효한 인증 코드가 존재하지 않습니다."),

    // 409 Conflict,
    MEMBER_ALREADY_REGISTERED("EDMT-4090101", "이미 등록된 회원입니다.");

    private final String code;
    private final String message;
}
