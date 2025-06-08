package com.edumate.eduserver.notice.controller.request;

import com.edumate.eduserver.notice.domain.NoticeCategory;
import java.time.LocalDateTime;

public record NoticeGetResponse(
        long noticeId,
        String category,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static NoticeGetResponse of(long noticeId,
                                       NoticeCategory noticeCategory,
                                       String title,
                                       String content,
                                       LocalDateTime createdAt) {
        return new NoticeGetResponse(noticeId, noticeCategory.getText(), title, content, createdAt);
    }
}
