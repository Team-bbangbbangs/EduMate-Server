package com.edumate.eduserver.util;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.repository.MemberStudentRecordRepository;
import com.edumate.eduserver.studentrecord.repository.StudentRecordDetailRepository;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.repository.SubjectRepository;
import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.repository.MemberRepository;
import com.edumate.eduserver.util.fixture.MemberFixture;
import com.edumate.eduserver.util.fixture.MemberStudentRecordFixture;
import com.edumate.eduserver.util.fixture.StudentRecordDetailFixture;
import com.edumate.eduserver.util.fixture.SubjectFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ServiceTest {

    protected Member defaultTeacher;
    protected StudentRecordDetail defaultRecordDetail;

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

        Subject korean = subjectRepository.save(SubjectFixture.korean());
        defaultTeacher = memberRepository.save(MemberFixture.teacher01(korean));
        MemberStudentRecord record01 = memberStudentRecordRepository.save(MemberStudentRecordFixture.studentRecord01(defaultTeacher));
        defaultRecordDetail = studentRecordDetailRepository.save(StudentRecordDetailFixture.detail01(record01));
        studentRecordDetailRepository.save(StudentRecordDetailFixture.detail02(record01));
    }
}
