package com.edumate.eduserver.auth.security.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
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

    private static final String USER_ROLE_CLAIM = "role";
    private static final String USER_ID_CLAIM = "userId";

    public String generateToken(final long userId, final String role, final TokenType tokenType) {
        Instant now = Instant.now();
        Instant expiration = generateExpirationInstant(tokenType, now);

        JwtBuilder builder = createJwtBuilder(userId, role, tokenType, now, expiration);

        return builder
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private JwtBuilder createJwtBuilder(final long userId, final String role, final TokenType tokenType,
                                        final Instant now, final Instant expiration) {
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(USER_ID_CLAIM, userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration));

        if (tokenType == TokenType.ACCESS) {
            builder.claim(USER_ROLE_CLAIM, role);
        }

        return builder;
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

