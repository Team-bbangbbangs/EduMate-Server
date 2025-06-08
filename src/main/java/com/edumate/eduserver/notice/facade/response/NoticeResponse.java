package com.edumate.eduserver.notice.facade.response;

import java.time.LocalDateTime;

public record NoticeResponse(
        long noticeId,
        String category,
        String title,
        LocalDateTime createdAt
) {
}
