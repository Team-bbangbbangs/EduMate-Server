package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.external.aws.ses.AwsSesProperties;
import com.edumate.eduserver.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final EmailService emailService;
    private final MemberService memberService;
    private final AwsSesProperties awsSesProperties;

    public void sendVerificationEmail(final String memberUuid) {
        String emailAddress = memberService.getMemberEmail(memberUuid);
        emailService.sendEmail(emailAddress, String.format(awsSesProperties.emailVerifyUrl(), memberUuid, "8888")); // 임시 코드(수정 예정)
    }
}
