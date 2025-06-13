package com.edumate.eduserver.notice.facade.response;

import java.time.LocalDateTime;

public record NoticeResponse(
        long noticeId,
        String category,
        String title,
        LocalDateTime createdAt
) {
    public static NoticeResponse of(final long noticeId, final String category, final String title, final LocalDateTime createdAt) {
        return new NoticeResponse(
                noticeId,
                category,
                title,
                createdAt
        );
    }
}
