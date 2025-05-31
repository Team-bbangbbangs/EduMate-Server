package com.edumate.eduserver.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonSuccessCode implements SuccessCode {

    SUCCESS(HttpStatus.OK, "EDMT-200", "요청이 성공했습니다."),
    CREATED(HttpStatus.CREATED, "EDMT-201", "요청이 성공했습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "EDMT-204", "요청이 성공했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
