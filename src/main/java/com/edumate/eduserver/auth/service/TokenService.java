package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.security.jwt.JwtGenerator;
import com.edumate.eduserver.auth.security.jwt.TokenType;
import com.edumate.eduserver.user.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final JwtGenerator jwtGenerator;

    @Transactional
    public Token generateTokens(final Member member) {
        String accessToken = jwtGenerator.generateToken(member.getMemberUuid(), TokenType.ACCESS);
        String refreshToken = jwtGenerator.generateToken(member.getMemberUuid(), TokenType.REFRESH);
        member.updateRefreshToken(refreshToken);
        return Token.of(accessToken, refreshToken);
    }
}
