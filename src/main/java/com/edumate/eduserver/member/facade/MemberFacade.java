package com.edumate.eduserver.member.facade;

import com.edumate.eduserver.auth.service.PasswordValidator;
import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.exception.InvalidPasswordException;
import com.edumate.eduserver.member.exception.PasswordSameAsOldException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
import com.edumate.eduserver.member.facade.response.MemberProfileGetResponse;
import com.edumate.eduserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public MemberProfileGetResponse getMemberProfile(final long memberId) {
        Member member = memberService.getMemberById(memberId);
        return MemberProfileGetResponse.of(
                member.getEmail(), member.getSubject().getName(), member.isVerifyTeacher(),
                member.getSchool().getName(), member.getNickname()
        );
    }

    public void updatePassword(final long memberId, final String currentPassword, final String newPassword) {
        Member member = memberService.getMemberById(memberId);
        PasswordValidator.validatePasswordFormat(newPassword);
        validatePassword(member, currentPassword, newPassword);
        member.updatePassword(passwordEncoder.encode(newPassword));
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
}
