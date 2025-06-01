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
    public ApiResponse<Void> handleBadRequestException(BadRequestException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(ConflictException.class)
    public ApiResponse<Void> handleCompletionException(ConflictException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse<Void> handleForbiddenException(ForbiddenException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<Void> handleNotFoundException(NotFoundException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse<Void> handleUnauthorizedException(UnauthorizedException e) {
        return ApiResponse.fail(e, e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneralException(Exception e) {
        return ApiResponse.fail("EDMT-500", e.getMessage());
    }
}
