package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.auth.security.jwt.JwtGenerator;
import com.edumate.eduserver.auth.security.jwt.TokenType;
import com.edumate.eduserver.auth.service.AuthService;
import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.auth.service.RandomCodeGenerator;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.service.SubjectService;
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final EmailService emailService;
    private final AuthService authService;
    private final MemberService memberService;
    private final SubjectService subjectService;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;
    private final RandomCodeGenerator randomCodeGenerator;

    public void sendVerificationEmail(final String memberUuid) {
        Member member = memberService.getMemberByUuid(memberUuid);
        String verificationCode = randomCodeGenerator.generate();
        authService.saveCode(member, verificationCode);
        emailService.sendEmail(member.getEmail(), memberUuid, verificationCode);
    }

    @Transactional
    public void verifyEmailCode(final String memberUuid, final String verificationCode) {
        Member member = memberService.getMemberByUuid(memberUuid);
        authService.verifyEmailCode(member, verificationCode);
        memberService.updateEmailVerified(member);
    }

    @Transactional
    public MemberSignUpResponse signUp(final String email, final String password, final String subjectName, final String school) {
        Subject subject = subjectService.getSubjectByName(subjectName);
        String encodedPassword = passwordEncoder.encode(password);
        authService.checkAlreadyRegistered(email);
        String memberUuid = memberService.saveMember(email, encodedPassword, subject, school);
        String accessToken = jwtGenerator.generateToken(memberUuid, TokenType.ACCESS);
        String refreshToken = jwtGenerator.generateToken(memberUuid, TokenType.REFRESH);
        return MemberSignUpResponse.of(accessToken, refreshToken);
    }
}
