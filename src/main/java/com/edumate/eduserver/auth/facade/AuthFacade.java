package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.facade.response.EmailVerifyResponse;
import com.edumate.eduserver.auth.service.AuthService;
import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.auth.service.RandomCodeGenerator;
import com.edumate.eduserver.auth.service.TokenService;
import com.edumate.eduserver.auth.service.dto.TokenDto;
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final EmailService emailService;
    private final AuthService authService;
    private final TokenService tokenService;
    private final MemberService memberService;
    private final RandomCodeGenerator randomCodeGenerator;

    public void sendVerificationEmail(final String memberUuid) {
        Member member = memberService.getMemberByUuid(memberUuid);
        String verificationCode = randomCodeGenerator.getCode(member);
        emailService.sendEmail(member.getEmail(), memberUuid, verificationCode);
    }

    @Transactional
    public EmailVerifyResponse verifyEmailCode(final String memberUuid, final String verificationCode) {
        Member member = memberService.getMemberByUuid(memberUuid);
        authService.verifyEmailCode(member, verificationCode);
        memberService.updateEmailVerified(member);
        TokenDto tokenDto = tokenService.generateTokens(member);
        return EmailVerifyResponse.of(tokenDto.accessToken(), tokenDto.refreshToken());
    }
}
