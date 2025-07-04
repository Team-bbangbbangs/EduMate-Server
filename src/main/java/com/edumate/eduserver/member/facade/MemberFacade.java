package com.edumate.eduserver.member.facade;

import com.edumate.eduserver.auth.service.AuthService;
import com.edumate.eduserver.auth.service.PasswordValidator;
import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.exception.InvalidPasswordException;
import com.edumate.eduserver.member.exception.PasswordSameAsOldException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
import com.edumate.eduserver.member.facade.dto.MemberEmailUpdatedEvent;
import com.edumate.eduserver.member.facade.response.MemberNicknameValidationResponse;
import com.edumate.eduserver.member.facade.response.MemberProfileGetResponse;
import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final SubjectService subjectService;
    private final AuthService authService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public MemberProfileGetResponse getMemberProfile(final long memberId) {
        Member member = memberService.getMemberById(memberId);
        return MemberProfileGetResponse.of(
                member.getEmail(), member.getSubject().getName(), member.isVerifyTeacher(),
                member.getSchool().getName(), member.getNickname()
        );
    }

    @Transactional
    public void updatePassword(final long memberId, final String currentPassword, final String newPassword) {
        Member member = memberService.getMemberById(memberId);
        PasswordValidator.validatePasswordFormat(newPassword);
        validatePassword(member, currentPassword, newPassword);
        member.updatePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void updateMemberProfile(final long memberId, final String subjectName, final School school, final String nickname) {
        Member member = memberService.getMemberById(memberId);
        Subject subject = subjectService.getSubjectByName(subjectName);
        memberService.updateMemberProfile(member, subject, school, nickname);
    }

    public MemberNicknameValidationResponse validateNickname(final long memberId, final String nickname) {
        Member member = memberService.getMemberById(memberId);
        return MemberNicknameValidationResponse.of(
                memberService.isNicknameInvalid(nickname),
                memberService.isNicknameDuplicated(member, nickname)
        );
    }

    @Transactional
    public void updateEmail(final long memberId, final String email) {
        validateEmail(email);
        Member member = memberService.getMemberById(memberId);
        memberService.updateEmail(member, email);
        memberService.setRolePendingTeacher(member);
        publishEmailUpdatedEvent(member);
    }

    private void validatePassword(final Member member, final String currentPassword, final String newPassword) {
        String savedPassword = member.getPassword();
        if (!isPasswordMatched(currentPassword, savedPassword)) {
            throw new InvalidPasswordException(MemberErrorCode.INVALID_CURRENT_PASSWORD);
        }
        if (isPasswordMatched(newPassword, savedPassword)) {
            throw new PasswordSameAsOldException(MemberErrorCode.SAME_PASSWORD);
        }
    }

    private boolean isPasswordMatched(final String inputPassword, final String savedPassword) {
        return passwordEncoder.matches(inputPassword, savedPassword);
    }

    private void validateEmail(String email) {
        authService.checkAlreadyRegistered(email);
        authService.validateEmail(email);
    }

    private void publishEmailUpdatedEvent(final Member member) {
        String authCode = authService.issueVerificationCode(member);
        eventPublisher.publishEvent(MemberEmailUpdatedEvent.of(member.getEmail(), member.getMemberUuid(), authCode));
    }
}
