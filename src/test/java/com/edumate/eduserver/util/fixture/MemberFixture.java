package com.edumate.eduserver.util.fixture;

import static com.edumate.eduserver.user.domain.Role.PENDING_TEACHER;
import static com.edumate.eduserver.user.domain.Role.TEACHER;
import static com.edumate.eduserver.user.domain.School.HIGH_SCHOOL;
import static com.edumate.eduserver.user.domain.School.MIDDLE_SCHOOL;

import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.user.domain.Member;

public class MemberFixture {

    public static Member teacher01(Subject subject) {
        return Member.create(subject, "teacher01@sen.go.kr", "test123445", "teacher01",
                HIGH_SCHOOL, TEACHER);
    }

    public static Member teacher02(Subject subject) {
        return Member.create(subject, "teacher02@sen.go.kr", "test123445", "teacher02",
                MIDDLE_SCHOOL, TEACHER);
    }

    public static Member teacher03(Subject subject) {
        return Member.create(subject, "teacher03@sen.go.kr", "test123445", "teacher03",
                HIGH_SCHOOL, PENDING_TEACHER);
    }
}

