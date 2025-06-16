package com.edumate.eduserver.notice.facade.response;

import java.time.LocalDateTime;

public record NoticeGetResponse(
        long noticeId,
        String category,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static NoticeGetResponse of(final long noticeId,
                                       final String category,
                                       final String title,
                                       final String content,
                                       final LocalDateTime createdAt) {
        return new NoticeGetResponse(
                noticeId,
                category,
                title,
                content,
                createdAt
        );
    }
}
