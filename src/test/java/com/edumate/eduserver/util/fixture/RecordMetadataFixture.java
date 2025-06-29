package com.edumate.eduserver.util.fixture;

import com.edumate.eduserver.studentrecord.domain.RecordMetadata;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.member.domain.Member;

public class RecordMetadataFixture {

    private static final String FIRST_SEMESTER = "2025-1";
    private static final String SECOND_SEMESTER = "2025-2";

    public static RecordMetadata studentRecord01(Member member) {
        return RecordMetadata.create(
                member,
                StudentRecordType.ABILITY_DETAIL,
                FIRST_SEMESTER
        );
    }

    public static RecordMetadata studentRecord02(Member member) {
        return RecordMetadata.create(
                member,
                StudentRecordType.BEHAVIOR_OPINION,
                FIRST_SEMESTER
        );
    }

    public static RecordMetadata studentRecord03(Member member) {
        return RecordMetadata.create(
                member,
                StudentRecordType.CREATIVE_ACTIVITY_AUTONOMY,
                SECOND_SEMESTER
        );
    }
}

