package com.edumate.eduserver.common.security;

import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        handleException(response);
    }

    private void handleException(final HttpServletResponse response) throws IOException {
        setResponse(response);
    }

    private void setResponse(final HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiResponse.fail(HttpStatus.UNAUTHORIZED.value(), AuthErrorCode.UNAUTHORIZED.getCode(),
                        AuthErrorCode.UNAUTHORIZED.getMessage())));
    }
}
