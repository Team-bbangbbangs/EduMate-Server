package com.edumate.eduserver.common.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RepositoryLoggingAspect {

    @Before("within(@org.springframework.stereotype.Repository *)")
    public void logRepositoryMethod(final JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        String requestId = org.slf4j.MDC.get("requestId");
        String userId = org.slf4j.MDC.get("userId");

        log.info("[REPO] requestId={} userId={} {}.{}({})",
                requestId, userId, className, methodName, argsToString(args));
    }

    private String argsToString(final Object[] args) {
        if (args == null || args.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
} 
