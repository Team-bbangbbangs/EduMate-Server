package com.edumate.eduserver.common.log;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MdcLoggingFilter.class);

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        try {
            MDC.put("logType", "REQUEST");
            chain.doFilter(request, response);
            long duration = System.currentTimeMillis() - startTime;
            MDC.put("responseTimeMs", String.valueOf(duration));
            MDC.put("logType", "RESPONSE");
            if (request instanceof HttpServletRequest req && response instanceof HttpServletResponse res) {
                log.info("[API] {} {} | status={} | responseTimeMs={}", req.getMethod(), req.getRequestURI(), res.getStatus(), duration);
            } else {
                log.info("[API] Non-HTTP request | responseTimeMs={}", duration);
            }
        } finally {
            MDC.remove("requestId");
            MDC.remove("responseTimeMs");
            MDC.remove("logType");
            MDC.remove("userId");
        }
    }
} 
