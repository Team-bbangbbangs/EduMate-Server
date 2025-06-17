package com.edumate.eduserver.member.facade;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.facade.response.MemberProfileGetResponse;
import com.edumate.eduserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;

    public MemberProfileGetResponse getMemberProfile(final long memberId) {
        Member member = memberService.getMemberById(memberId);
        return MemberProfileGetResponse.of(
                member.getEmail(), member.getSubject().getName(), member.isVerifyTeacher(), member.getSchool().getName()
        );
    }
}
