package com.edumate.eduserver.common;

import com.edumate.eduserver.common.code.SuccessCode;
import com.edumate.eduserver.common.exception.EduMateCustomException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final int status;
    private final String code;
    private final String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiResponse<T> success(final SuccessCode success) {
        return new ApiResponse<>(success.getStatus().value(), success.getCode(), success.getMessage());
    }

    public static <T> ApiResponse<T> success(final SuccessCode success, final T data) {
        return new ApiResponse<>(success.getStatus().value(), success.getCode(), success.getMessage(), data);
    }

    public static <T> ApiResponse<T> fail(final EduMateCustomException ex, final String code, final String message) {
        return new ApiResponse<>(ex.getStatus().value(), code, message);
    }

    public static <T> ApiResponse<T> fail(final int httpStatus, final String code, final String message) {
        return new ApiResponse<>(httpStatus, code, message);
    }
}
