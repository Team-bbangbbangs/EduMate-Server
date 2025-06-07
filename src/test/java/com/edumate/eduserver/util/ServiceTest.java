package com.edumate.eduserver.util;

import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.repository.SubjectRepository;
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.repository.MemberRepository;
import com.edumate.eduserver.util.fixture.MemberFixture;
import com.edumate.eduserver.util.fixture.SubjectFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ServiceTest {

    protected Subject defaultSubject;
    protected Member defaultMember;

    @Autowired
    private DbCleaner dbCleaner;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeEach
    void resetDb() {
        dbCleaner.truncateEveryTable();
        defaultSubject = subjectRepository.save(SubjectFixture.KOREAN);
        defaultMember = memberRepository.save(MemberFixture.TEACHER01);
    }
}
