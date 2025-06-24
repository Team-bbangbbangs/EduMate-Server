package com.edumate.eduserver.common.config;

import com.edumate.eduserver.auth.jwt.JwtParser;
import com.edumate.eduserver.auth.jwt.JwtValidator;
import com.edumate.eduserver.common.security.CustomPasswordEncoder;
import com.edumate.eduserver.common.security.JwtAccessDeniedHandler;
import com.edumate.eduserver.common.security.JwtAuthenticationEntryPoint;
import com.edumate.eduserver.common.security.filter.ExceptionHandlerFilter;
import com.edumate.eduserver.common.security.filter.JwtAuthenticationFilter;
import com.edumate.eduserver.member.service.MemberAuthenticationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final MemberAuthenticationService memberAuthenticationService;
    private final JwtParser jwtParser;
    private final JwtValidator jwtValidator;

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/signup",
            "/api/v1/auth/login",
            "/api/v1/auth/reissue",
            "/api/v1/auth/password",
            "/api/v1/auth/verify-email",
            "/api/v1/auth/find-password",
            "/actuator/health"
    };
    private static final String[] BUSINESS_WHITE_LIST = {
            "/api/v1/notices",
            "/api/v1/notices/{noticeId:\\d+}"
    };

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(BUSINESS_WHITE_LIST).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/student-records/**").hasAnyRole("TEACHER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(memberAuthenticationService, jwtValidator, jwtParser,
                                getWhitelistPaths(), new AntPathMatcher()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JwtAuthenticationFilter.class)
                .build();
    }

    private String[] getWhitelistPaths() {
        List<String> allWhitelist = new ArrayList<>();
        allWhitelist.addAll(Arrays.asList(AUTH_WHITELIST));
        allWhitelist.addAll(Arrays.asList(BUSINESS_WHITE_LIST));
        return allWhitelist.toArray(new String[0]);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(allowedOrigin));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
