package com.edumate.eduserver.user.service;

import com.edumate.eduserver.user.domain.Member;
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

    public Member getMemberByUuid(final String memberUuid) {
        return findByUuid(memberUuid);
    }

    private Member findByUuid(final String memberUuid) {
        return memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
