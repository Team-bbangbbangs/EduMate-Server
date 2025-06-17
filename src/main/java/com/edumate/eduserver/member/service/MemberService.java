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
        return memberRepository.findByEmail(email)
                .filter(Member::isDeleted)
                .map(member -> {
                    member.restore(password, subject, school);
                    return member;
                });
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
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean isAdmin(final Member member) {
        return member.getRole() == Role.ADMIN;
    }
}
