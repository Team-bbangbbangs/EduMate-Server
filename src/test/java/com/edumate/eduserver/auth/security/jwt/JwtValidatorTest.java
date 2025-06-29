package com.edumate.eduserver.auth.security.jwt;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.edumate.eduserver.auth.exception.ExpiredTokenException;
import com.edumate.eduserver.auth.exception.IllegalTokenTypeException;
import com.edumate.eduserver.auth.exception.InvalidSignatureTokenException;
import com.edumate.eduserver.auth.jwt.JwtGenerator;
import com.edumate.eduserver.auth.jwt.JwtProperties;
import com.edumate.eduserver.auth.jwt.JwtValidator;
import com.edumate.eduserver.auth.jwt.TokenType;
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
        String memberUuid = "q24234";
        String token = jwtGenerator.generateToken(memberUuid, TokenType.ACCESS);

        // then
        assertDoesNotThrow(() -> jwtValidator.validateToken(token, TokenType.ACCESS));
    }

    @Test
    @DisplayName("Refresh 토큰으로 검증을 시도할 경우 예외가 발생한다.")
    void validateRefreshToken_success() {
        // given
        String memberUuid = "aasf";
        String token = jwtGenerator.generateToken(memberUuid, TokenType.REFRESH);

        // when & then
        assertThatThrownBy(() -> jwtValidator.validateToken(token, TokenType.ACCESS))
                .isInstanceOf(IllegalTokenTypeException.class);
    }

    @Test
    @DisplayName("만료된 토큰은 예외가 발생한다")
    void validateToken_expired() {
        // given
        String memberUuid = "asdf";

        // when
        String expiredToken = Jwts.builder()
                .setSubject(memberUuid)
                .setIssuedAt(Date.from(Instant.now().minusSeconds(60)))
                .setExpiration(Date.from(Instant.now().minusSeconds(30)))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secretKey())), SignatureAlgorithm.HS256)
                .compact();

        // then
        assertThatThrownBy(() -> jwtValidator.validateToken(expiredToken, TokenType.ACCESS))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("잘못된 서명의 토큰은 예외가 발생한다")
    void validateToken_invalidSignature() {
        // given
        String memberUuid = "adsfasdf";

        // when
        String fakeSecret = Base64.getEncoder().encodeToString(FAKE_SECRET_KEY.getBytes());
        String token = Jwts.builder()
                .setSubject(memberUuid)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(60)))
                .signWith(hmacShaKeyFor(Base64.getDecoder().decode(fakeSecret)), SignatureAlgorithm.HS256)
                .compact();

        // then
        assertThatThrownBy(() -> jwtValidator.validateToken(token, TokenType.ACCESS))
                .isInstanceOf(InvalidSignatureTokenException.class);
    }
}
