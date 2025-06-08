package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.BusinessErrorCode;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EduMateCustomException.class)
    public ApiResponse<Void> handleCustomException(final EduMateCustomException e) {
        String message = e.getCustomMessage() != null ? e.getCustomMessage() : e.getErrorCode().getMessage();
        return ApiResponse.fail(e, e.getErrorCode().getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnexpectedException(final Exception e) {
        Throwable cause = getDeepCause(e);
        if (cause instanceof EduMateCustomException) {
            return handleCustomException((EduMateCustomException) cause);
        }
        log.error("Unhandled Exception: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), BusinessErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(final MethodArgumentNotValidException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    private Throwable getDeepCause(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }
}
