package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.BusinessErrorCode;
import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.common.exception.ConflictException;
import com.edumate.eduserver.common.exception.ForbiddenException;
import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.common.exception.UnauthorizedException;
import javax.swing.Spring;
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

    @ExceptionHandler(BadRequestException.class)
    public ApiResponse<Void> handleBadRequestException(final BadRequestException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(ConflictException.class)
    public ApiResponse<Void> handleConflictException(final ConflictException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse<Void> handleForbiddenException(final ForbiddenException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<Void> handleNotFoundException(final NotFoundException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse<Void> handleUnauthorizedException(final UnauthorizedException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    // Spring 프레임워크에서 발생하는 공통 예외를 처리합니다.

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<Void> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException: {}", getRootCauseMessage(e), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException: {}", getRootCauseMessage(e), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.warn("MethodArgumentTypeMismatchException: {}", getRootCauseMessage(e), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<Void> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.warn("HttpRequestMethodNotSupportedException: {}", getRootCauseMessage(e), e);
        return ApiResponse.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), BusinessErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Void> handleNoResourceFoundException(final NoResourceFoundException e) {
        log.warn("NoResourceFoundException: {}", getRootCauseMessage(e), e);
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), BusinessErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", getRootCauseMessage(e), e);
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), BusinessErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneralException(final Exception e) {
        log.error("Unhandled Exception: {}", getRootCauseMessage(e), e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), BusinessErrorCode.INTERNAL_SERVER_ERROR);
    }

    private String getRootCauseMessage(final Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}
