package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.exception.IllegalTokenException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.security.jwt.JwtGenerator;
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

    private static final String BEARER_PREFIX = "Bearer ";

    @Transactional
    public Token generateTokens(final Member member) {
        String accessToken = jwtGenerator.generateToken(member.getMemberUuid(), TokenType.ACCESS);
        String refreshToken = jwtGenerator.generateToken(member.getMemberUuid(), TokenType.REFRESH);
        member.updateRefreshToken(refreshToken);
        return Token.of(accessToken, refreshToken);
    }

    public String getRemovedBearerPrefixToken(final String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        throw new IllegalTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE);
    }
}
