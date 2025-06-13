package com.edumate.eduserver.user.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    // 400 Bad Request
    INVALID_SCHOOL_TYPE("EDMT-4000401", "입력하신 %s는 유효하지 않습니다. MIDDLE, HIGH 중 하나를 입력해주세요."),

    // 404 Not Found
    MEMBER_NOT_FOUND("EDMT-4040401", "해당 회원을 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
