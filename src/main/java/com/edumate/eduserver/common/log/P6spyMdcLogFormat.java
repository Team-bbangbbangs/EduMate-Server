package com.edumate.eduserver.common.log;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.slf4j.MDC;

public class P6spyMdcLogFormat implements MessageFormattingStrategy {
    @Override
    public String formatMessage(
            int connectionId, String now, long elapsed, String category,
            String prepared, String sql, String url) {
        if (sql == null || sql.trim().isEmpty()) return "";

        String requestId = MDC.get("requestId");
        String userId = MDC.get("userId");

        return String.format("[SQL] requestId=%s userId=%s | %s | %dms | %s",
                requestId != null ? requestId : "none",
                userId != null ? userId : "anonymous",
                category, elapsed, sql.trim());
    }
} 