package com.edumate.eduserver.notice.facade.response;

import java.time.LocalDateTime;

public record NoticeResponse(
        long noticeId,
        String category,
        String title,
        LocalDateTime createdAt
) {
    public static NoticeResponse of(long noticeId, String category, String title, LocalDateTime createdAt) {
        return new NoticeResponse(
                noticeId,
                category,
                title,
                createdAt
        );
    }
}
