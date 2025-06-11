package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.auth.service.RandomCodeGenerator;
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final EmailService emailService;
    private final MemberService memberService;
    private final RandomCodeGenerator randomCodeGenerator;

    public void sendVerificationEmail(final String memberUuid) {
        Member member = memberService.getMemberByUuid(memberUuid);
        String verificationCode = randomCodeGenerator.getCode(member);
        emailService.sendEmail(member.getEmail(), memberUuid, verificationCode);
    }
}
