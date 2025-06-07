package com.edumate.eduserver.studentrecord.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StudentRecordType {

    ABILITY_DETAIL("ACTIVITY"),
    BEHAVIOR_OPINION("BEHAVIOR"),
    CREATIVE_ACTIVITY_CAREER("CREATIVE-CAREER"),
    CREATIVE_ACTIVITY_AUTONOMY("CREATIVE-AUTONOMY"),
    CREATIVE_ACTIVITY_CLUB("CREATIVE-CLUB");

    private final String value;
}
