package com.edumate.eduserver.notice.facade.response;

import com.edumate.eduserver.notice.service.dto.NoticeDto;
import java.time.LocalDateTime;

public record NoticeGetResponse(
        long noticeId,
        String category,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static NoticeGetResponse of(NoticeDto noticeDto) {
        return new NoticeGetResponse(
                noticeDto.noticeId(),
                noticeDto.category().getText(),
                noticeDto.title(),
                noticeDto.content(),
                noticeDto.createdAt()
        );
    }
}
