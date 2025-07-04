package com.edumate.eduserver.member.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    // 400 Bad Request
    INVALID_SCHOOL_TYPE("EDMT-4000401", "입력하신 %s는 유효하지 않습니다. MIDDLE, HIGH 중 하나를 입력해주세요."),
    INVALID_CURRENT_PASSWORD("EDMT-4000402", "현재 비밀번호가 일치하지 않습니다. 다시 입력해주세요."),
    SAME_PASSWORD("EDMT-4000403", "새로운 비밀번호는 기존 비밀번호와 같을 수 없습니다."),
    INVALID_NICKNAME("EDMT-4000404", "입력하신 %s은(는) 유효하지 않은 닉네임입니다."),
    DUPLICATED_NICKNAME("EDMT-4000405", "입력하신 %s은(는) 중복된 닉네임입니다."),

    // 404 Not Found
    MEMBER_NOT_FOUND("EDMT-4040401", "존재하지 않는 회원입니다. 회원가입을 진행해주세요.");

    private final String code;
    private final String message;
}
