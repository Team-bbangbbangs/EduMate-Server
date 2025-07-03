package com.edumate.eduserver.auth.facade;

import com.edumate.eduserver.auth.exception.MemberAlreadyRegisteredException;
import com.edumate.eduserver.auth.exception.MismatchedPasswordException;
import com.edumate.eduserver.auth.exception.PasswordSameAsOldException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.facade.dto.MemberSignedUpEvent;
import com.edumate.eduserver.auth.facade.response.MemberLoginResponse;
import com.edumate.eduserver.auth.facade.response.MemberReissueResponse;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.auth.jwt.TokenType;
import com.edumate.eduserver.auth.service.AuthService;
import com.edumate.eduserver.auth.service.PasswordValidator;
import com.edumate.eduserver.auth.service.TokenService;
import com.edumate.eduserver.external.aws.mail.EmailService;
import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public void sendVerificationEmail(final long memberId) {
        Member member = memberService.getMemberById(memberId);
        String verificationCode = authService.issueVerificationCode(member);
        emailService.sendEmail(member.getEmail(), member.getMemberUuid(), verificationCode);
    }

    @Transactional
    public void verifyEmailCode(final String memberUuid, final String verificationCode) {
        Member member = memberService.getMemberByUuid(memberUuid);
        authService.verifyEmailCode(member, verificationCode);
        memberService.updateEmailVerified(member);
    }

    @Transactional
    public MemberLoginResponse login(final String email, final String password) {
        Member member = memberService.getMemberByEmail(email);
        if (!isPasswordMatched(password, member.getPassword())) {
            throw new MismatchedPasswordException(AuthErrorCode.INVALID_PASSWORD);
        }
        boolean isAdmin = memberService.isAdmin(member);
        String accessToken = tokenService.generateTokens(member, TokenType.ACCESS);
        String refreshToken = tokenService.generateTokens(member, TokenType.REFRESH);
        memberService.updateRefreshToken(member, refreshToken);
        return MemberLoginResponse.of(accessToken, refreshToken, isAdmin);
    }

    @Transactional
    public void logout(final long memberId) {
        Member member = memberService.getMemberById(memberId);
        authService.logout(member);
    }

    @Transactional
    public MemberReissueResponse reissue(final String refreshToken) {
        String memberUuid = tokenService.getMemberUuidFromToken(refreshToken);
        Member member = memberService.getMemberByUuid(memberUuid);
        try {
            tokenService.checkTokenEquality(refreshToken, member.getRefreshToken());
            String newAccessToken = tokenService.generateTokens(member, TokenType.ACCESS);
            String newRefreshToken = tokenService.generateTokens(member, TokenType.REFRESH);
            memberService.updateRefreshToken(member, newRefreshToken);
            boolean isAdmin = memberService.isAdmin(member);
            return MemberReissueResponse.of(newAccessToken, newRefreshToken, isAdmin);
        } catch (Exception e) {
            logout(member.getId());
            throw e;
        }
    }

    @Transactional
    public MemberSignUpResponse signUp(final String email, final String password, final String subjectName,
                                       final String school) {
        checkPreconditions(email, password);
        Subject subject = subjectService.getSubjectByName(subjectName);
        Member member = createMember(email, password, subject, school);
        String accessToken = tokenService.generateTokens(member, TokenType.ACCESS);
        String refreshToken = tokenService.generateTokens(member, TokenType.REFRESH);
        memberService.updateRefreshToken(member, refreshToken);
        String authCode = authService.issueVerificationCode(member);

        eventPublisher.publishEvent(MemberSignedUpEvent.of(member.getEmail(), member.getMemberUuid(), authCode));
        return MemberSignUpResponse.of(accessToken, refreshToken);
    }

    private void checkPreconditions(final String email, final String password) {
        authService.checkAlreadyRegistered(email);
        authService.validateEmail(email);
        PasswordValidator.validatePasswordFormat(password);
    }

    private Member createMember(final String email, final String password, final Subject subject, final String school) {
        String encodedPassword = passwordEncoder.encode(password);
        try {
            return memberService.saveMember(email, encodedPassword, subject, school);
        } catch (DataIntegrityViolationException e) {
            throw new MemberAlreadyRegisteredException(AuthErrorCode.MEMBER_ALREADY_REGISTERED);
        }
    }

    @Transactional
    public void updatePassword(final String email, final String newPassword) {
        PasswordValidator.validatePasswordFormat(newPassword);
        Member member = memberService.getMemberByEmail(email);
        if (isPasswordMatched(newPassword, member.getPassword())) {
            throw new PasswordSameAsOldException(AuthErrorCode.SAME_PASSWORD);
        }
        memberService.updatePassword(member, passwordEncoder.encode(newPassword));
    }

    private boolean isPasswordMatched(final String rawPassword, final String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void findPassword(final String email) {
        memberService.checkExistedEmail(email);
    }
}
