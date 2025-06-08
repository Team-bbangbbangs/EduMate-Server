package com.edumate.eduserver.util.fixture;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.user.domain.Member;

public class MemberStudentRecordFixture {

    private static final String FIRST_SEMESTER = "2025-1";
    private static final String SECOND_SEMESTER = "2025-2";

    public static MemberStudentRecord studentRecord01(Member member) {
        return MemberStudentRecord.create(
                member,
                StudentRecordType.ABILITY_DETAIL,
                FIRST_SEMESTER
        );
    }

    public static MemberStudentRecord studentRecord02(Member member) {
        return MemberStudentRecord.create(
                member,
                StudentRecordType.BEHAVIOR_OPINION,
                FIRST_SEMESTER
        );
    }

    public static MemberStudentRecord studentRecord03(Member member) {
        return MemberStudentRecord.create(
                member,
                StudentRecordType.CREATIVE_ACTIVITY_AUTONOMY,
                SECOND_SEMESTER
        );
    }
}

