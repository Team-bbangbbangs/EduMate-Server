package com.edumate.eduserver.util.fixture;

import static com.edumate.eduserver.util.fixture.MemberStudentRecordFixture.STUDENT_RECORD_01;
import static com.edumate.eduserver.util.fixture.MemberStudentRecordFixture.STUDENT_RECORD_02;
import static com.edumate.eduserver.util.fixture.MemberStudentRecordFixture.STUDENT_RECORD_03;

import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;

public class StudentRecordDetailFixture {

    public static final StudentRecordDetail STUDENT_RECORD_DETAIL_01 = StudentRecordDetail.create(
            STUDENT_RECORD_01, "2020314912", "김가연",
            "학생은 수업에 적극적으로 참여하고 있으며, 학습 능력이 뛰어납니다.", 10
    );

    public static final StudentRecordDetail STUDENT_RECORD_DETAIL_02 = StudentRecordDetail.create(
            STUDENT_RECORD_02, "2020314913", "이승섭",
            "학생은 수업에 적극적으로 참여하고 있으며, 학습 능력이 우수합니다.", 9
    );

    public static final StudentRecordDetail STUDENT_RECORD_DETAIL_03 = StudentRecordDetail.create(
            STUDENT_RECORD_03, "2020314914", "유태근",
            "학생의 능력에 대한 상세한 의견입니다. 학생은 수업에 적극적으로 참여하고 있으며, 학습 능력이 양호합니다.", 8
    );
}
