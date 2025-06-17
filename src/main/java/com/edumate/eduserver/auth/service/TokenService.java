package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.security.jwt.JwtGenerator;
import com.edumate.eduserver.auth.security.jwt.JwtParser;
import com.edumate.eduserver.auth.security.jwt.JwtValidator;
import com.edumate.eduserver.auth.security.jwt.TokenType;
import com.edumate.eduserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final JwtGenerator jwtGenerator;
    private final JwtParser jwtParser;
    private final JwtValidator jwtValidator;

    @Transactional
    public Token generateTokens(final Member member) {
        String memberUuid = member.getMemberUuid();
        String accessToken = jwtGenerator.generateToken(memberUuid, TokenType.ACCESS);
        String refreshToken = jwtGenerator.generateToken(memberUuid, TokenType.REFRESH);
        member.updateRefreshToken(refreshToken);
        return Token.of(accessToken, refreshToken);
    }

    public String getMemberUuidFromToken(final String refreshToken) {
        String prefixRemovedToken = jwtParser.resolveToken(refreshToken);
        jwtValidator.validateToken(prefixRemovedToken, TokenType.REFRESH);
        return jwtParser.getMemberUuidFromToken(prefixRemovedToken);
    }
}
