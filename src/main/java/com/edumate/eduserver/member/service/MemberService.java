package com.edumate.eduserver.member.service;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.domain.Role;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.exception.MemberNotFoundException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
import com.edumate.eduserver.member.repository.MemberRepository;
import com.edumate.eduserver.subject.domain.Subject;
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
    public String saveMember(final String email, final String password, final Subject subject,
                             final String schoolName) {
        School school = School.fromName(schoolName);
        Member member = Member.create(subject, email, password, DEFAULT_NICKNAME, school, INITIAL_ROLE);
        memberRepository.save(member);
        String generatedNickname = DEFAULT_NICKNAME + member.getId();
        member.updateNickname(generatedNickname);
        return member.getMemberUuid();
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
