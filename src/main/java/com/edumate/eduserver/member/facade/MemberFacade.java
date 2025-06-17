package com.edumate.eduserver.member.facade;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.facade.response.MemberProfileGetResponse;
import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final StudentRecordService studentRecordService;

    public MemberProfileGetResponse getMemberProfile(final long memberId) {
        Member member = memberService.getMemberById(memberId);
        MemberStudentRecord memberStudentRecord = studentRecordService.getLatestMemberStudentRecord(memberId);
        return MemberProfileGetResponse.of(
                member.getEmail(), member.getSubject().getName(), member.isVerifyTeacher(),
                member.getSchool().getName(), member.getNickname(), memberStudentRecord.getSemester()
        );
    }
}
