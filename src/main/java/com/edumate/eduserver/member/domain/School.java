package com.edumate.eduserver.member.domain;

import com.edumate.eduserver.member.exception.InvalidSchoolException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum School {
    MIDDLE_SCHOOL("middle"),
    HIGH_SCHOOL("high");

    private final String name;

    public static School fromName(final String name) {
        return Arrays.stream(School.values())
                .filter(school -> school.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new InvalidSchoolException(MemberErrorCode.INVALID_SCHOOL_TYPE, name));
    }
}
