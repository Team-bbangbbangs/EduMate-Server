package com.edumate.eduserver.notice.service.dto;

import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import java.time.LocalDateTime;

public record NoticeDto(
        long noticeId,
        NoticeCategory category,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static NoticeDto of(final Notice notice) {
        return new NoticeDto(
                notice.getId(),
                notice.getCategory(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt()
        );
    }
}
