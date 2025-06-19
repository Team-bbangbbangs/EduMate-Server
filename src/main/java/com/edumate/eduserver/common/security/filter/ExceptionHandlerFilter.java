package com.edumate.eduserver.common.security.filter;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.ErrorCode;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String INTERNAL_SERVER_ERROR_CUSTOM_CODE = "EDMT-500";

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(request, response);
        } catch (EduMateCustomException e) {
            handleCustomException(response, e);
        } catch (Exception ex) {
            handleException(response, ex);
        }
    }

    private void handleCustomException(final HttpServletResponse response, final Exception e) throws IOException {
        EduMateCustomException ux = (EduMateCustomException) e;
        ErrorCode authErrorCode = ux.getErrorCode();
        HttpStatus httpStatus = ux.getStatus();
        setResponse(response, httpStatus, authErrorCode.getCode(), authErrorCode.getMessage());
    }

    private void handleException(final HttpServletResponse response, final Exception e) throws IOException {
        setResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_CUSTOM_CODE, e.getMessage());
    }

    private void setResponse(final HttpServletResponse response, final HttpStatus httpStatus,
                             final String customCode, final String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(httpStatus.value());
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(ApiResponse.fail(httpStatus.value(), customCode, message)));
    }
}

