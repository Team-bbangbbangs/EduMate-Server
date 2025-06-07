package com.edumate.eduserver.util;

import com.edumate.eduserver.studentrecord.repository.MemberStudentRecordRepository;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import com.edumate.eduserver.subject.repository.SubjectRepository;
import com.edumate.eduserver.user.repository.MemberRepository;
import com.edumate.eduserver.util.fixture.MemberFixture;
import com.edumate.eduserver.util.fixture.MemberStudentRecordFixture;
import com.edumate.eduserver.util.fixture.StudentRecordDetailFixture;
import com.edumate.eduserver.util.fixture.SubjectFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ServiceTest {

    @Autowired
    private DbCleaner dbCleaner;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private MemberStudentRecordRepository memberStudentRecordRepository;

    @Autowired
    private StudentRecordDetailRepository studentRecordDetailRepository;

    @BeforeEach
    void resetDb() {
        dbCleaner.truncateEveryTable();
        subjectRepository.saveAll(List.of(SubjectFixture.KOREAN, SubjectFixture.MATH, SubjectFixture.ENGLISH));
        memberRepository.saveAll(List.of(MemberFixture.TEACHER_01, MemberFixture.TEACHER_02, MemberFixture.TEACHER_03));
        memberStudentRecordRepository.saveAll(List.of(
                MemberStudentRecordFixture.STUDENT_RECORD_01,
                MemberStudentRecordFixture.STUDENT_RECORD_02,
                MemberStudentRecordFixture.STUDENT_RECORD_03)
        );
        studentRecordDetailRepository.saveAll(List.of(
                StudentRecordDetailFixture.STUDENT_RECORD_DETAIL_01,
                StudentRecordDetailFixture.STUDENT_RECORD_DETAIL_02,
                StudentRecordDetailFixture.STUDENT_RECORD_DETAIL_03)
        );
    }
}
