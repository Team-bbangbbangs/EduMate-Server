package com.edumate.eduserver.user.domain;

import com.edumate.eduserver.user.exception.InvalidSchoolException;
import com.edumate.eduserver.user.exception.code.MemberErrorCode;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum School {
    MIDDLE_SCHOOL("MIDDLE"),
    HIGH_SCHOOL("HIGH");

    private final String name;

    public static School fromName(final String name) {
        return Arrays.stream(School.values())
                .filter(school -> school.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new InvalidSchoolException(MemberErrorCode.INVALID_SCHOOL_TYPE, name));
    }
}
