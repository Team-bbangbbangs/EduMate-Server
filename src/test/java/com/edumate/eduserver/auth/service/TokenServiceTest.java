package com.edumate.eduserver.auth.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.edumate.eduserver.auth.exception.MismatchedTokenException;
import com.edumate.eduserver.auth.jwt.JwtGenerator;
import com.edumate.eduserver.auth.jwt.JwtParser;
import com.edumate.eduserver.auth.jwt.JwtValidator;
import com.edumate.eduserver.auth.jwt.TokenType;
import com.edumate.eduserver.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TokenServiceTest {

    @Mock
    private JwtGenerator jwtGenerator;
    @Mock
    private JwtParser jwtParser;
    @Mock
    private JwtValidator jwtValidator;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("토큰이 정상적으로 생성된다.")
    void generateTokens() {
        Member member = mock(Member.class);
        when(member.getMemberUuid()).thenReturn("uuid-1234");
        when(jwtGenerator.generateToken("uuid-1234", TokenType.ACCESS)).thenReturn("access-token");
        when(jwtGenerator.generateToken("uuid-1234", TokenType.REFRESH)).thenReturn("refresh-token");

        Token token = tokenService.generateTokens(member);

        verify(member).updateRefreshToken("refresh-token");
        assertThat(token.accessToken()).isEqualTo("access-token");
        assertThat(token.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("refreshToken에서 memberUuid를 정상적으로 추출한다.")
    void getMemberUuidFromToken() {
        String refreshToken = "Bearer refresh-token";
        String resolvedToken = "refresh-token";
        when(jwtParser.resolveToken(refreshToken)).thenReturn(resolvedToken);
        doNothing().when(jwtValidator).validateToken(resolvedToken, TokenType.REFRESH);
        when(jwtParser.getMemberUuidFromToken(resolvedToken)).thenReturn("uuid-1234");

        String uuid = tokenService.getMemberUuidFromToken(refreshToken);
        assertThat(uuid).isEqualTo("uuid-1234");
    }

    @Test
    @DisplayName("토큰이 정상적으로 검증된다.")
    void checkTokenEquality() {
        // given
        String requestRefreshToken = "Bearer refresh-token";
        String storedRefreshToken = "refresh-token";
        String resolvedToken = "refresh-token";

        when(jwtParser.resolveToken(requestRefreshToken)).thenReturn(resolvedToken);
        doNothing().when(jwtValidator).validateToken(resolvedToken, TokenType.REFRESH);

        // when & then
        tokenService.checkTokenEquality(requestRefreshToken, storedRefreshToken);
        verify(jwtParser).resolveToken(requestRefreshToken);
    }

    @Test
    @DisplayName("저장된 토큰과 요청 토큰이 불일치하면 예외가 발생한다.")
    void tokenEqualityMismatch() {
        // given
        String requestRefreshToken = "Bearer refresh-token";
        String storedRefreshToken = "different-refresh-token";
        String resolvedToken = "refresh-token";

        when(jwtParser.resolveToken(requestRefreshToken)).thenReturn(resolvedToken);
        doNothing().when(jwtValidator).validateToken(resolvedToken, TokenType.REFRESH);

        // when & then
        assertThatThrownBy(() -> tokenService.checkTokenEquality(requestRefreshToken, storedRefreshToken))
            .isInstanceOf(MismatchedTokenException.class);
    }
}
