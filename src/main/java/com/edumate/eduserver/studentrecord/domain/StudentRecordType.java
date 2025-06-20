package com.edumate.eduserver.studentrecord.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentRecordType {

    ABILITY_DETAIL("SUBJECT", 1500), // 과세특
    BEHAVIOR_OPINION("BEHAVIOR", 1500), // 행발
    CREATIVE_ACTIVITY_CAREER("CAREER", 1500), // 진로
    CREATIVE_ACTIVITY_AUTONOMY("FREE", 1500), // 자율
    CREATIVE_ACTIVITY_CLUB("CLUB", 1500); // 동아리

    private final String value;
    private final int byteCount;
}
