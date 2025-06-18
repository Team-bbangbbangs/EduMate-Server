package com.edumate.eduserver.common.security.filter;

import com.edumate.eduserver.auth.jwt.JwtParser;
import com.edumate.eduserver.auth.jwt.JwtValidator;
import com.edumate.eduserver.auth.jwt.TokenType;
import com.edumate.eduserver.common.security.MemberAuthentication;
import com.edumate.eduserver.member.service.MemberAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MemberAuthenticationService memberAuthService;
    private final AntPathMatcher pathMatcher;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;
    private final String[] whitelistPaths;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            return true;
        }
        return Arrays.stream(whitelistPaths)
                .anyMatch(whitelist -> pathMatcher.match(whitelist, path));
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        String requestedToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = jwtParser.resolveToken(requestedToken);
        jwtValidator.validateToken(token, TokenType.ACCESS);
        String memberUuid = jwtParser.parseClaims(token).getSubject();
        UserDetails userDetails = memberAuthService.loadUserByUsername(memberUuid);
        long memberId = Long.parseLong(userDetails.getUsername());
        MemberAuthentication authentication = MemberAuthentication.create(memberId, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
