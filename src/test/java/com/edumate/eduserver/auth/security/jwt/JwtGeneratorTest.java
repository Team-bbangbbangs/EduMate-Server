package com.edumate.eduserver.auth.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("JWT Token 생성 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtGeneratorTest {

    private static final String ROLE_CLAIM = "role";
    private static final String USER_ID_CLAIM = "userId";

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    JwtParser jwtParser;

    @Test
    @DisplayName("Access 토큰이 정상적으로 생성되고, Claims가 올바르게 들어간다")
    void generateAccessToken_success() {
        // given
        long userId = 123L;
        String role = "TEACHER";

        // when
        String token = jwtGenerator.generateToken(userId, role, TokenType.ACCESS);

        // then
        Claims claims = jwtParser.parseClaims(token);

        assertAll(
            () -> assertThat(claims.get(USER_ID_CLAIM, Integer.class).longValue()).isEqualTo(userId),
            () -> assertThat(claims.get(ROLE_CLAIM, String.class)).isEqualTo(role),
            () -> assertThat(claims.getExpiration()).isNotNull(),
            () -> assertThat(claims.getIssuedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("Refresh 토큰은 role 없이 userId만 들어간다")
    void generateRefreshToken_success() {
        // given
        long userId = 456L;
        String role = "ADMIN";

        // when
        String token = jwtGenerator.generateToken(userId, role, TokenType.REFRESH);

        // then
        Claims claims = jwtParser.parseClaims(token);

        assertAll(
            () -> assertThat(claims.get(USER_ID_CLAIM, Integer.class).longValue()).isEqualTo(userId),
            () -> assertThat(claims.get(ROLE_CLAIM)).isNull(),
            () -> assertThat(claims.getExpiration()).isNotNull()
        );
    }
}
