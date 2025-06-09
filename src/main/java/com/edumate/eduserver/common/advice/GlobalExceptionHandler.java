package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EduMateCustomException.class)
    public ApiResponse<Void> handleCustomException(final EduMateCustomException e) {
        String message = e.getCustomMessage() != null ? e.getCustomMessage() : e.getErrorCode().getMessage();
        return ApiResponse.fail(e, e.getErrorCode().getCode(), message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        Throwable cause = getDeepCause(e);
        if (cause instanceof EduMateCustomException) {
            return handleCustomException((EduMateCustomException) cause);
        }
        log.warn("MethodArgumentTypeMismatchException: {}", e.getMessage());
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), "EDMT-4001", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(final MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : e.getMessage();
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), "EDMT-4002", message);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnexpectedException(final Exception e) {
        log.error("Unhandled Exception: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "EDMT-500", e.getMessage());
    }

    private Throwable getDeepCause(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }
}
