package com.edumate.eduserver.user.service;

import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.domain.Role;
import com.edumate.eduserver.user.domain.School;
import com.edumate.eduserver.user.exception.MemberNotFoundException;
import com.edumate.eduserver.user.exception.code.MemberErrorCode;
import com.edumate.eduserver.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private static final Role INITIAL_ROLE = Role.PENDING_TEACHER;
    private static final String DEFAULT_NICKNAME = "닉네임";

    @Transactional
    public void updateEmailVerified(final Member member) {
        member.verifyAsTeacher();
    }

    @Transactional
    public String saveMember(final String email, final String password, final Subject subject,
                             final String schoolName) {
        School school = School.fromName(schoolName);
        Member member = Member.create(subject, email, DEFAULT_NICKNAME, password, school, INITIAL_ROLE);
        memberRepository.saveAndFlush(member);

        String generatedNickname = DEFAULT_NICKNAME + member.getId();
        member.updateNickname(generatedNickname);
        return member.getMemberUuid();
    }

    public Member getMemberByUuid(final String memberUuid) {
        return memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
