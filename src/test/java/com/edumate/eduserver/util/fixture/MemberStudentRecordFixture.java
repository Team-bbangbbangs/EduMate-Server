package com.edumate.eduserver.util.fixture;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;

public class MemberStudentRecordFixture {

    private static final String FIRST_SEMESTER = "2025-1";
    private static final String SECOND_SEMESTER = "2025-2";

    public static final MemberStudentRecord STUDENT_RECORD_01 = MemberStudentRecord.create(
            MemberFixture.TEACHER_01,
            StudentRecordType.ABILITY_DETAIL,
            FIRST_SEMESTER
    );

    public static final MemberStudentRecord STUDENT_RECORD_02 = MemberStudentRecord.create(
            MemberFixture.TEACHER_02,
            StudentRecordType.BEHAVIOR_OPINION,
            FIRST_SEMESTER
    );

    public static final MemberStudentRecord STUDENT_RECORD_03 = MemberStudentRecord.create(
            MemberFixture.TEACHER_03,
            StudentRecordType.CREATIVE_ACTIVITY_AUTONOMY,
            SECOND_SEMESTER
    );
}
