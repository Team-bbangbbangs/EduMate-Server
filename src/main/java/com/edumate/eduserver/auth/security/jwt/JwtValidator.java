package com.edumate.eduserver.auth.security.jwt;

import com.edumate.eduserver.auth.exception.ExpiredTokenException;
import com.edumate.eduserver.auth.exception.IllegalTokenException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtParser jwtParser;

    private static final String TOKEN_TYPE_CLAIM = "tokenType";

    public void validateToken(final String token, final TokenType type) {
        try {
            Claims claims = jwtParser.parseClaims(token);
            validateClaims(claims, type);
        } catch (ExpiredJwtException e) {
            throw getExpiredTokenException(type);
        } catch (IllegalTokenException e) {
            throw e;
        } catch (Exception e) {
            throw getInvalidTokenException(type);
        }
    }

    private void validateClaims(final Claims claims, final TokenType expectedType) {
        String memberUuid = claims.getSubject();
        if (memberUuid == null || memberUuid.isBlank()) {
            throw new IllegalTokenException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }
        String parsedTokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        if (!expectedType.getType().equals(parsedTokenType)) {
            throw new IllegalTokenException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    private ExpiredTokenException getExpiredTokenException(final TokenType type) {
        if (type == TokenType.ACCESS) {
            return new ExpiredTokenException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } else {
            return new ExpiredTokenException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    private IllegalTokenException getInvalidTokenException(final TokenType type) {
        if (type == TokenType.ACCESS) {
            return new IllegalTokenException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        } else {
            return new IllegalTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE);
        }
    }
}
