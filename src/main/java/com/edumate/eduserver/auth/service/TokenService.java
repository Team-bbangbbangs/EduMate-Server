package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.exception.MismatchedTokenException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.jwt.JwtGenerator;
import com.edumate.eduserver.auth.jwt.JwtParser;
import com.edumate.eduserver.auth.jwt.JwtValidator;
import com.edumate.eduserver.auth.jwt.TokenType;
import com.edumate.eduserver.member.domain.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken";

    @Transactional
    public String generateTokens(final Member member, final TokenType tokenType) {
        String memberUuid = member.getMemberUuid();
        return jwtGenerator.generateToken(memberUuid, tokenType);
    }

    public String getMemberUuidFromToken(final String refreshToken) {
        String prefixRemovedToken = jwtParser.resolveToken(refreshToken);
        jwtValidator.validateToken(prefixRemovedToken, TokenType.REFRESH);
        return jwtParser.getMemberUuidFromToken(prefixRemovedToken);
    }

    public void checkTokenEquality(final String requestRefreshToken, final String storedRefreshToken) {
        String prefixRemovedToken = jwtParser.resolveToken(requestRefreshToken);
        if (!isTokenMatched(prefixRemovedToken, storedRefreshToken)) {
            throw new MismatchedTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE);
        }
    }

    private boolean isTokenMatched(final String requestRefreshToken, final String storedRefreshToken) {
        return MessageDigest.isEqual(
                storedRefreshToken.getBytes(StandardCharsets.UTF_8),
                requestRefreshToken.getBytes(StandardCharsets.UTF_8)
        );
    }

    public void setRefreshTokenCookie(final HttpServletResponse response, final String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_PREFIX, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(14 * 24 * 60 * 60);

        response.addCookie(cookie);
    }
}
