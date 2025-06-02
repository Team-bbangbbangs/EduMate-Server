package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.exception.BadRequestException;
import com.edumate.eduserver.common.exception.ConflictException;
import com.edumate.eduserver.common.exception.ForbiddenException;
import com.edumate.eduserver.common.exception.NotFoundException;
import com.edumate.eduserver.common.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneralException(final Exception e) {
        return ApiResponse.fail("EDMT-500", e.getMessage());
    }
}
