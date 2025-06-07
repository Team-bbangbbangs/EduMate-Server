package com.edumate.eduserver.notice.controller.response;

import com.edumate.eduserver.notice.domain.Notice;
import java.util.List;
import org.springframework.data.domain.Page;

public record NoticesGetResponse(
        int totalPages,
        List<NoticeResponse> notices
) {
    public static NoticesGetResponse of(final Page<Notice> noticePage) {
        List<NoticeResponse> notices = noticePage.getContent()
                .stream()
                .map(notice -> new NoticeResponse(
                        notice.getId(),
                        notice.getCategory().getText(),
                        notice.getTitle(),
                        notice.getCreatedAt()
                ))
                .toList();
        return new NoticesGetResponse(
                noticePage.getTotalPages(),
                notices
        );
    }
}
