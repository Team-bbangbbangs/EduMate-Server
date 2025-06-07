package com.edumate.eduserver.notice.controller.response;

import java.time.LocalDateTime;

public record NoticeResponse(
        Long noticeId,
        String category,
        String title,
        LocalDateTime createdAt
) {
}
