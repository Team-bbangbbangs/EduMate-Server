package com.edumate.eduserver.studentrecord.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StudentRecordType {

    ABILITY_DETAIL("세부능력 및 특기사항"),
    BEHAVIOR_OPINION("행동특성 및 종합의견"),
    CREATIVE_ACTIVITY_CAREER("창의적 체험활동 - 진로"),
    CREATIVE_ACTIVITY_AUTONOMY("창의적 체험활동 - 자율"),
    CREATIVE_ACTIVITY_CLUB("창의적 체험활동 - 동아리");

    private final String description;
}
