package com.edumate.eduserver.external.ai.exception.code;

import com.edumate.eduserver.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OpenAiErrorCode implements ErrorCode {

    // 408 - Request Timeout
    CONNECTION_TIMEOUT("EDMT-4080601", "OpenAI 서버에 연결할 수 없습니다. 네트워크 상태를 확인해 주세요."),
    READ_TIMEOUT("EDMT-4080602", "OpenAI 응답 시간이 초과되었습니다. 잠시 후 다시 시도해 주세요."),

    // 429 - Too Many Requests
    RATE_LIMIT_EXCEEDED("EDMT-4290601", "요청이 너무 많습니다. 잠시 후 다시 시도해 주세요."),
    QUOTA_EXCEEDED("EDMT-4290602", "사용량이 초과되었습니다. 잠시 후 다시 시도해 주세요."),

    // 500 - Internal Server Error
    UNKNOWN_ERROR("EDMT-5000601", "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.")
    ;

    private final String code;
    private final String message;
}
