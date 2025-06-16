package com.edumate.eduserver.notice.controller.request;

import jakarta.validation.constraints.NotNull;

public record NoticeCreateRequest(
        @NotNull(message = "카테고리 ID는 필수입니다.")
        Integer categoryId,
        @NotNull(message = "제목은 필수입니다.")
        String title,
        @NotNull(message = "내용은 필수입니다.")
        String content
) {
    public static NoticeCreateRequest of(final int categoryId,
                                       final String title,
                                       final String content) {
        return new NoticeCreateRequest(
                categoryId,
                title,
                content
        );
    }
}
