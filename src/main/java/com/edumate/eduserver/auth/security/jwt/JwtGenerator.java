package com.edumate.eduserver.auth.security.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtGenerator {

    private final JwtProperties jwtProperties;

    private static final String MEMBER_UUID_CLAIM = "member_uuid";

    public String generateToken(final String userId, final TokenType tokenType) {
        Instant now = Instant.now();
        Instant expiration = generateExpirationInstant(tokenType, now);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(MEMBER_UUID_CLAIM, userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Instant generateExpirationInstant(final TokenType tokenType, final Instant now) {
        return now.plusMillis(calculateExpirationTime(tokenType));
    }

    private long calculateExpirationTime(final TokenType tokenType) {
        if (tokenType == TokenType.ACCESS) {
            return jwtProperties.accessTokenExpiration();
        } else {
            return jwtProperties.refreshTokenExpiration();
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

