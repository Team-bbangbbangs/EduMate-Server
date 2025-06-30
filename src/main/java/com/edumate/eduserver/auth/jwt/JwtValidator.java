package com.edumate.eduserver.auth.jwt;

import com.edumate.eduserver.auth.exception.ExpiredTokenException;
import com.edumate.eduserver.auth.exception.IllegalTokenException;
import com.edumate.eduserver.auth.exception.IllegalTokenTypeException;
import com.edumate.eduserver.auth.exception.InvalidSignatureTokenException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
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
            throw new ExpiredTokenException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            if (type == TokenType.ACCESS) {
                throw new IllegalTokenException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
            }
            throw new IllegalTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE);
        } catch (SignatureException e) {
            throw new InvalidSignatureTokenException(AuthErrorCode.INVALID_SIGNATURE_TOKEN);
        } catch (IllegalTokenException | IllegalTokenTypeException e) {
            throw new IllegalTokenException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw e;
        }
    }

    private void validateClaims(final Claims claims, final TokenType expectedType) {
        String memberUuid = claims.getSubject();
        String parsedTokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);

        if (!expectedType.getType().equals(parsedTokenType)) {
            throw new IllegalTokenTypeException(AuthErrorCode.INVALID_TOKEN_TYPE);
        }

        if (memberUuid == null || memberUuid.isBlank()) {
            if (expectedType == TokenType.ACCESS) {
                throw new IllegalTokenException(AuthErrorCode.INVALID_ACCESS_TOKEN_VALUE);
            }
            throw new IllegalTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE);
        }
    }
}
