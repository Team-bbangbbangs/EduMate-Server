package com.edumate.eduserver.member.service;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.domain.Role;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.exception.MemberNotFoundException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
import com.edumate.eduserver.member.repository.MemberRepository;
import com.edumate.eduserver.subject.domain.Subject;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private static final Role INITIAL_ROLE = Role.PENDING_TEACHER;
    private static final String DEFAULT_NICKNAME = "선생님";
    private static final boolean NOT_DELETED = false;
    private static final boolean DELETED = true;

    @Transactional
    public void updateEmailVerified(final Member member) {
        member.verifyAsTeacher();
    }

    @Transactional
    public Member saveMember(final String email, final String password, final Subject subject,
                             final String schoolName) {
        School school = School.fromName(schoolName);

        Optional<Member> restored = restoreIfSoftDeletedMemberByEmail(email, password, subject, school);
        if (restored.isPresent()) {
            return restored.get();
        }

        Member newMember = Member.create(subject, email, password, DEFAULT_NICKNAME, school, INITIAL_ROLE);
        memberRepository.save(newMember);
        String generatedNickname = DEFAULT_NICKNAME + newMember.getId();
        newMember.updateNickname(generatedNickname);
        return newMember;
    }

    private Optional<Member> restoreIfSoftDeletedMemberByEmail(final String email, final String password,
                                                               final Subject subject, final School school) {
        return memberRepository.findByEmailAndIsDeleted(email, DELETED)
                .map(member -> {
                    member.restore(password, subject, school);
                    return member;
                });
    }

    @Transactional
    public void updatePassword(final Member member, final String encodedPassword) {
        member.updatePassword(encodedPassword);
    }

    @Transactional
    public void updateRefreshToken(final Member member, final String refreshToken) {
        member.updateRefreshToken(refreshToken);
    }

    public Member getMemberById(final long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getMemberByUuid(final String memberUuid) {
        return memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getMemberByEmail(final String email) {
        return memberRepository.findByEmailAndIsDeleted(email, NOT_DELETED)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public void checkExistedEmail(final String email) {
        if (!memberRepository.existsByEmailAndIsDeleted(email, NOT_DELETED)) {
            throw new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }

    public boolean isAdmin(final Member member) {
        return member.getRole() == Role.ADMIN;
    }

    @Transactional
    public void updateMemberProfile(final long memberId, final Subject subject, final School school, final String nickname) {
        Member member = getMemberById(memberId);
        member.updateProfile(subject, school, nickname);
    }
}
