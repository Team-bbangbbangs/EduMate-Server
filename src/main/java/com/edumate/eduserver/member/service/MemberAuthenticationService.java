package com.edumate.eduserver.member.service;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.exception.MemberNotFoundException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
import com.edumate.eduserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthenticationService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String memberUuid) {
        try {
            Member member = getMemberByUuid(memberUuid);
            String memberId = String.valueOf(member.getId());
            return new User(memberId, member.getPassword(), member.getAuthorities());
        } catch (MemberNotFoundException e) {
            throw new UsernameNotFoundException("Member not found with UUID: " + memberUuid, e);
        }
    }

    private Member getMemberByUuid(final String memberUuid) {
        return memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
