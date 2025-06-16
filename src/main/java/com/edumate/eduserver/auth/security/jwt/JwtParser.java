package com.edumate.eduserver.auth.security.jwt;

import com.edumate.eduserver.auth.exception.MissingTokenException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtParser {

    private final JwtProperties jwtProperties;

    private static final String BEARER = "Bearer ";

    public Claims parseClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(final String token) {
        if (token != null && token.startsWith(BEARER)) {
            return token.substring(BEARER.length());
        }
        throw new MissingTokenException(AuthErrorCode.MISSED_TOKEN);
    }

    public String getMemberUuidFromToken(final String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        return claims.getSubject();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
