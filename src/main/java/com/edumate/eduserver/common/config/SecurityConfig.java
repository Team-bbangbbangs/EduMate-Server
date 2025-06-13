package com.edumate.eduserver.common.config;

import com.edumate.eduserver.auth.security.jwt.JwtParser;
import com.edumate.eduserver.auth.security.jwt.JwtValidator;
import com.edumate.eduserver.common.security.JwtAuthenticationEntryPoint;
import com.edumate.eduserver.common.security.filter.ExceptionHandlerFilter;
import com.edumate.eduserver.common.security.filter.JwtAuthenticationFilter;
import com.edumate.eduserver.user.service.MemberAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final MemberAuthenticationService memberAuthService;
    private final JwtValidator jwtValidator;
    private final JwtParser jwtParser;

    private static final String[] authWhiteList = {"/api/v1/auth/signup", "/api/v1/auth/reissue", "/actuator/health",
            "api/v1/auth/verify-email"};
    private static final String[] businessLogicWhiteList = {"api/v1/notices", "api/v1/notices/{noticeId:\\d+}"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .anyRequest()
                                .authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(memberAuthService, jwtValidator, jwtParser),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(authWhiteList)
                .requestMatchers(HttpMethod.GET, businessLogicWhiteList);
    }
}
