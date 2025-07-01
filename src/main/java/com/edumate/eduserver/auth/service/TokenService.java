package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.exception.MismatchedTokenException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.jwt.JwtGenerator;
import com.edumate.eduserver.auth.jwt.JwtParser;
import com.edumate.eduserver.auth.jwt.JwtValidator;
import com.edumate.eduserver.auth.jwt.TokenType;
import com.edumate.eduserver.member.domain.Member;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenService {

    private final JwtGenerator jwtGenerator;
    private final JwtParser jwtParser;
    private final JwtValidator jwtValidator;

    public String generateTokens(final Member member, final TokenType tokenType) {
        String memberUuid = member.getMemberUuid();
        return jwtGenerator.generateToken(memberUuid, tokenType);
    }

    public String getMemberUuidFromToken(final String refreshToken) {
        jwtValidator.validateToken(refreshToken, TokenType.REFRESH);
        return jwtParser.getMemberUuidFromToken(refreshToken);
    }

    public void checkTokenEquality(final String requestRefreshToken, final String storedRefreshToken) {
        if (!isTokenMatched(requestRefreshToken, storedRefreshToken)) {
            throw new MismatchedTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE);
        }
    }

    private boolean isTokenMatched(final String requestRefreshToken, final String storedRefreshToken) {
        if (requestRefreshToken == null || storedRefreshToken == null) {
            return false;
        }

        return MessageDigest.isEqual(
                storedRefreshToken.getBytes(StandardCharsets.UTF_8),
                requestRefreshToken.getBytes(StandardCharsets.UTF_8)
        );
    }
}
