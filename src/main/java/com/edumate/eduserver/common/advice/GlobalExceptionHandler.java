package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import software.amazon.awssdk.core.exception.SdkException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatusCode status,
                                                                  final WebRequest request) {
        FieldError fieldError = ex.getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : "유효하지 않은 요청입니다.";
        return new ResponseEntity<>(ApiResponse.fail(status.value(), "EDMT-4002", message), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatusCode status,
                                                                          final WebRequest request) {
        return new ResponseEntity<>(ApiResponse.fail(status.value(), "EDMT-4001", ex.getMessage()), headers, status);
    }

    @ExceptionHandler(SdkException.class)
    public ApiResponse<Void> handleSdkException(final SdkException e) {
        log.error("AWS SDK Exception: {}", getDeepCause(e).getMessage(), e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "EDMT-50201", e.getMessage());
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
