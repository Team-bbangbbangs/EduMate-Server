package com.edumate.eduserver.common.code;

public interface ErrorCode {

    String getCode();
    String getMessage();

    default String formatMessage(final Object... args) {
        return String.format(getMessage(), args);
    }
}
