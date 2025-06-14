package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.exception.MemberAlreadyRegisteredException;
import com.edumate.eduserver.auth.exception.MismatchedPasswordException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.facade.response.MemberLoginResponse;
import com.edumate.eduserver.auth.facade.response.MemberReissueResponse;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.auth.security.jwt.JwtParser;
import com.edumate.eduserver.auth.security.jwt.JwtValidator;
import com.edumate.eduserver.auth.security.jwt.TokenType;
import com.edumate.eduserver.auth.service.AuthService;
import com.edumate.eduserver.auth.service.EmailService;
import com.edumate.eduserver.auth.service.PasswordValidator;
import com.edumate.eduserver.auth.service.RandomCodeGenerator;
import com.edumate.eduserver.auth.service.Token;
import com.edumate.eduserver.auth.service.TokenService;
import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final TokenService tokenService;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
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
    public MemberSignUpResponse signUp(final String email, final String password, final String subjectName,
                                       final String school) {
        authService.checkAlreadyRegistered(email);
        PasswordValidator.validate(password);
        String encodedPassword = passwordEncoder.encode(password);
        Subject subject = subjectService.getSubjectByName(subjectName);
        try {
            String memberUuid = memberService.saveMember(email, encodedPassword, subject, school);
            Token token = tokenService.generateTokens(memberService.getMemberByUuid(memberUuid));
            return MemberSignUpResponse.of(token.accessToken(), token.refreshToken());
        } catch (DataIntegrityViolationException e) {
            throw new MemberAlreadyRegisteredException(AuthErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }

    public MemberLoginResponse login(final String email, final String password) {
        Member member = memberService.getMemberByEmail(email);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new MismatchedPasswordException(AuthErrorCode.MISMATCHED_PASSWORD);
        }
        Token token = tokenService.generateTokens(member);
        return MemberLoginResponse.of(token.accessToken(), token.refreshToken());
    }

    @Transactional
    public void logout(final String memberUuid) {
        Member member = memberService.getMemberByUuid(memberUuid);
        authService.logout(member);
    }

    @Transactional
    public MemberReissueResponse reissue(final String inputRefreshToken) {
        String refreshToken = tokenService.getRemovedBearerPrefixToken(inputRefreshToken);
        jwtValidator.validateToken(refreshToken, TokenType.REFRESH);
        String memberUuid= jwtParser.getMemberUuidFromToken(refreshToken, TokenType.REFRESH);
        Member member = memberService.getMemberByUuid(memberUuid);
        Token token = tokenService.generateTokens(member);
        return MemberReissueResponse.of(token.accessToken(), token.refreshToken());
    }
}
