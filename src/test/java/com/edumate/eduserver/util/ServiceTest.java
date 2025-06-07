package com.edumate.eduserver.util;

import com.edumate.eduserver.subject.repository.SubjectRepository;
import com.edumate.eduserver.user.repository.MemberRepository;
import com.edumate.eduserver.util.fixture.MemberFixture;
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

    @BeforeEach
    void resetDb() {
        dbCleaner.truncateEveryTable();
        subjectRepository.saveAll(List.of(SubjectFixture.KOREAN, SubjectFixture.MATH, SubjectFixture.ENGLISH));
        memberRepository.saveAll(List.of(MemberFixture.TEACHER01, MemberFixture.TEACHER02, MemberFixture.TEACHER03));
    }
}
