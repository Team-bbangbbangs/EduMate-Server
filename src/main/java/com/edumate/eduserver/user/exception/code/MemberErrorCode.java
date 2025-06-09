package com.edumate.eduserver.user.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    // 404 Not Found
    MEMBER_NOT_FOUND("EDMT-4040401", "해당 회원을 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
