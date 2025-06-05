package com.edumate.eduserver.auth.security.jwt;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.edumate.eduserver.auth.exception.IllegalTokenException;
import com.edumate.eduserver.common.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("JWT Token 검증 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtValidatorTest {

    private static final String ROLE_CLAIM = "role";
    private static final String USER_ID_CLAIM = "userId";
    private static final String FAKE_SECRET_KEY = "anothersecretkeyforjwt1234567890";

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    JwtValidator jwtValidator;

    @Autowired
    JwtProperties jwtProperties;

    @Test
    @DisplayName("정상 Access 토큰은 예외 없이 통과한다")
    void validateToken_success() {
        // given & when
        long userId = 1L;
        String role = "TEACHER";
        String token = jwtGenerator.generateToken(userId, role, TokenType.ACCESS);

        // then
        assertDoesNotThrow(() -> jwtValidator.validateToken(token, TokenType.ACCESS));
    }

    @Test
    @DisplayName("정상 Refresh 토큰은 예외 없이 통과한다")
    void validateRefreshToken_success() {
        // given & when
        long userId = 2L;
        String role = "ADMIN";
        String token = jwtGenerator.generateToken(userId, role, TokenType.REFRESH);

        // then
        assertDoesNotThrow(() -> jwtValidator.validateToken(token, TokenType.REFRESH));
    }

    @Test
    @DisplayName("만료된 토큰은 예외가 발생한다")
    void validateToken_expired() {
        // given
        long userId = 3L;
        String role = "TEACHER";

        // when
        String expiredToken = Jwts.builder()
                .claim(USER_ID_CLAIM, userId)
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(Date.from(Instant.now().minusSeconds(60)))
                .setExpiration(Date.from(Instant.now().minusSeconds(30)))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secretKey())), SignatureAlgorithm.HS256)
                .compact();

        // then
        assertThatThrownBy(() -> jwtValidator.validateToken(expiredToken, TokenType.ACCESS))
                .isInstanceOf(IllegalTokenException.class);
    }

    @Test
    @DisplayName("role 클레임이 없는 Access 토큰은 예외가 발생한다")
    void validateToken_noRoleClaim() {
        // given
        long userId = 4L;

        String token = Jwts.builder()
                .claim(USER_ID_CLAIM, userId)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(60)))
                .signWith(hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secretKey())), SignatureAlgorithm.HS256)
                .compact();

        // when & then
        assertThatThrownBy(() -> jwtValidator.validateToken(token, TokenType.ACCESS))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("잘못된 서명의 토큰은 예외가 발생한다")
    void validateToken_invalidSignature() {
        // given
        long userId = 5L;
        String role = "TEACHER";

        // when
        String fakeSecret = Base64.getEncoder().encodeToString(FAKE_SECRET_KEY.getBytes());
        String token = Jwts.builder()
                .claim(USER_ID_CLAIM, userId)
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(60)))
                .signWith(hmacShaKeyFor(Base64.getDecoder().decode(fakeSecret)), SignatureAlgorithm.HS256)
                .compact();

        // then
        assertThatThrownBy(() -> jwtValidator.validateToken(token, TokenType.ACCESS))
                .isInstanceOf(IllegalTokenException.class);
    }
}
