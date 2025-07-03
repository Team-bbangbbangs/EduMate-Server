package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import software.amazon.awssdk.core.exception.SdkException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EduMateCustomException.class)
    public ApiResponse<Void> handleCustomException(final EduMateCustomException e) {
        log.info("EduMateCustomException: {}", e.getMessage());
        String message = e.getCustomMessage() != null ? e.getCustomMessage() : e.getErrorCode().getMessage();
        return ApiResponse.fail(e, e.getErrorCode().getCode(), message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        Throwable cause = getDeepCause(e);
        if (cause instanceof EduMateCustomException) {
            return handleCustomException((EduMateCustomException) cause);
        }
        log.info("MethodArgumentTypeMismatchException: {}", e.getMessage());
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), "EDMT-4001", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(final MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : e.getMessage();
        log.info("Validation Error: {}", message);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), "EDMT-4002", message);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ApiResponse<Void> handleMissingRequestCookieException(final MissingRequestCookieException e) {
        log.warn("Missing Request Cookie: {}", e.getMessage());
        return ApiResponse.fail(HttpStatus.UNAUTHORIZED.value(), "EDMT-4001", e.getMessage());
    }

    @ExceptionHandler(SdkException.class)
    public ApiResponse<Void> handleSdkException(final SdkException e) {
        log.error("AWS SDK Exception: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "EDMT-50201", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnexpectedException(Exception e) {
        e = (Exception) getDeepCause(e);
        log.error("Unhandled Exception: {}", e.getMessage(), e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "EDMT-500", e.getMessage());
    }

    private Throwable getDeepCause(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }
}
