package com.edumate.eduserver.auth.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("JWT Token 생성 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtGeneratorTest {

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    JwtParser jwtParser;

    @Test
    @DisplayName("Access 토큰이 정상적으로 생성되는지 확인한다.")
    void generateAccessToken_success() {
        // given
        String memberUuid = "123L";

        // when
        String token = jwtGenerator.generateToken(memberUuid, TokenType.ACCESS);

        // then
        Claims claims = jwtParser.parseClaims(token);

        assertAll(
                () -> assertThat(claims.getSubject()).isEqualTo(memberUuid),
                () -> assertThat(claims.getExpiration()).isNotNull(),
                () -> assertThat(claims.getIssuedAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("Refresh 토큰이 정상적으로 생성되는지 확인한다.")
    void generateRefreshToken_success() {
        // given
        String memberUuid = "456L";

        // when
        String token = jwtGenerator.generateToken(memberUuid, TokenType.REFRESH);

        // then
        Claims claims = jwtParser.parseClaims(token);

        assertAll(
                () -> assertThat(claims.getSubject()).isEqualTo(memberUuid),
                () -> assertThat(claims.getExpiration()).isNotNull()
        );
    }
}
