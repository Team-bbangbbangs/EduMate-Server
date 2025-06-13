package com.edumate.eduserver.user.service;

import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.exception.MemberNotFoundException;
import com.edumate.eduserver.user.exception.code.MemberErrorCode;
import com.edumate.eduserver.user.repository.MemberRepository;
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
    public UserDetails loadUserByUsername(final String memberUuid) throws UsernameNotFoundException {
        Member member = getMemberByUuid(memberUuid);

        return new User(member.getMemberUuid(), member.getPassword(), member.getAuthorities());
    }

    private Member getMemberByUuid(final String memberUuid) {
        return memberRepository.findByMemberUuid(memberUuid)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
