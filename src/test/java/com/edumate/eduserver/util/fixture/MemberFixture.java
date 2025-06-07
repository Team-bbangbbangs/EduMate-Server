package com.edumate.eduserver.util.fixture;

import static com.edumate.eduserver.user.domain.Role.PENDING_TEACHER;
import static com.edumate.eduserver.user.domain.Role.TEACHER;
import static com.edumate.eduserver.user.domain.School.HIGH_SCHOOL;
import static com.edumate.eduserver.user.domain.School.MIDDLE_SCHOOL;
import static com.edumate.eduserver.util.fixture.SubjectFixture.ENGLISH;
import static com.edumate.eduserver.util.fixture.SubjectFixture.KOREAN;
import static com.edumate.eduserver.util.fixture.SubjectFixture.MATH;

import com.edumate.eduserver.user.domain.Member;

public class MemberFixture {

    public static final Member TEACHER01 = Member.create(KOREAN, "teacher01@sen.go.kr", "test123445", "teacher01",
            HIGH_SCHOOL, TEACHER);

    public static final Member TEACHER02 = Member.create(MATH, "teacher02@sen.go.kr", "test123445", "teacher02",
            MIDDLE_SCHOOL, TEACHER);

    public static final Member TEACHER03 = Member.create(ENGLISH, "teacher03@sen.go.kr", "test123445", "teacher03",
            HIGH_SCHOOL, PENDING_TEACHER);
}
