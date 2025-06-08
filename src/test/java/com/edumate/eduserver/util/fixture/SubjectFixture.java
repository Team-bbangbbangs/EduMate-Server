package com.edumate.eduserver.util.fixture;

import com.edumate.eduserver.subject.domain.Subject;

public class SubjectFixture {

    public static Subject korean() {
        return Subject.create("국어");
    }

    public static Subject math() {
        return Subject.create("수학");
    }

    public static Subject english() {
        return Subject.create("영어");
    }
}

