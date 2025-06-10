package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private static final String EMAIL_VERIFY_URL = "https://example.com/verify?memberId=%s&token=%s"; // Domain 구입 시 변경 예정

    private final EmailService emailService;
    private final MemberService memberService;

    public void sendVerificationEmail(final String memberUuid) {
        String emailAddress = memberService.getMemberEmail(memberUuid);
        emailService.sendEmail(emailAddress, String.format(EMAIL_VERIFY_URL, memberUuid, "8888")); // 임시 코드(수정 예정)
    }
}
