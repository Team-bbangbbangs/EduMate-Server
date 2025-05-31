package com.edumate.eduserver.common.advice;

import com.edumate.eduserver.common.code.ErrorCode;
import com.edumate.eduserver.common.code.SuccessCode;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final int status;
    private final String code;
    private final String message;
    private T data;

    public static <T> ApiResponse<T> success(final SuccessCode success) {
        return new ApiResponse<>(success.getStatus().value(), success.getCode(), success.getMessage());
    }

    public static <T> ApiResponse<T> success(final SuccessCode success, T data) {
        return new ApiResponse<>(success.getStatus().value(), success.getCode(), success.getMessage(), data);
    }

    public static <T> ApiResponse<T> fail(final EduMateCustomException ex, final ErrorCode error) {
        return new ApiResponse<>(ex.getStatus().value(), error.getCode(), error.getMessage());
    }
}
