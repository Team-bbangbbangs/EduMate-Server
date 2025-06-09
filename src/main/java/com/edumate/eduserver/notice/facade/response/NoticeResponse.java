package com.edumate.eduserver.notice.facade.response;

import com.edumate.eduserver.notice.service.dto.NoticeDto;
import java.time.LocalDateTime;

public record NoticeResponse(
        long noticeId,
        String category,
        String title,
        LocalDateTime createdAt
) {
    public static NoticeResponse of(final NoticeDto noticeDto) {
        return new NoticeResponse(
                noticeDto.noticeId(),
                noticeDto.category().getText(),
                noticeDto.title(),
                noticeDto.createdAt()
        );
    }
}
