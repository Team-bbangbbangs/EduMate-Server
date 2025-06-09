package com.edumate.eduserver.notice.facade.response;

import com.edumate.eduserver.notice.service.dto.NoticeDto;
import java.util.List;

public record NoticesGetResponse(
        int totalPages,
        List<NoticeResponse> notices
) {
    public static NoticesGetResponse of(final int totalPages, final List<NoticeDto> notices) {
        return new NoticesGetResponse(
                totalPages,
                notices.stream().map(NoticeResponse::of).toList()
        );
    }
}
