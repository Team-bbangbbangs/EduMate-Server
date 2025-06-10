package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final EmailService emailService;
    private final MemberService memberService;

    public void sendVerificationEmail(final String memberUuid) {
        String emailAddress = memberService.getMemberEmail(memberUuid);
        emailService.sendEmail(emailAddress, memberUuid, "8888"); // 임시 코드 하드코딩 (수정 예정)
    }
}
