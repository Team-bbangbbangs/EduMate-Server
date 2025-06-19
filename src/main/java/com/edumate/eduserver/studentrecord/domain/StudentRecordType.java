package com.edumate.eduserver.studentrecord.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentRecordType {

    ABILITY_DETAIL("ABILITY", 1500),
    BEHAVIOR_OPINION("BEHAVIOR", 1500),
    CREATIVE_ACTIVITY_CAREER("CREATIVE-CAREER", 1500),
    CREATIVE_ACTIVITY_AUTONOMY("CREATIVE-AUTONOMY", 1500),
    CREATIVE_ACTIVITY_CLUB("CREATIVE-CLUB", 1500);

    private final String value;
    private final int byteCount;
}
