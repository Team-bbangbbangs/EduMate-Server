package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.BusinessErrorCode;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EduMateCustomException.class)
    public ApiResponse<Void> handleCustomException(final EduMateCustomException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    // Spring 프레임워크에서 발생하는 공통 예외를 처리합니다.

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        Throwable cause = getDeepCause(e);
        if (cause instanceof EduMateCustomException) {
            return handleCustomException((EduMateCustomException) cause);
        }
        log.warn("MethodArgumentTypeMismatchException: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Void> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<Void> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.warn("HttpRequestMethodNotSupportedException: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), BusinessErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Void> handleNoResourceFoundException(final NoResourceFoundException e) {
        log.warn("NoResourceFoundException: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), BusinessErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnexpectedException(final Exception e) {
        log.error("Unhandled Exception: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), BusinessErrorCode.INTERNAL_SERVER_ERROR);
    }

    private Throwable getDeepCause(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }
}
