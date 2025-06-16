package com.edumate.eduserver.notice.facade.response;

import java.util.List;

public record NoticesGetResponse(
        int totalPages,
        List<NoticeResponse> notices
) {
    public static NoticesGetResponse of(final int totalPages, final List<NoticeResponse> notices) {
        return new NoticesGetResponse(
                totalPages,
                notices
        );
    }
}
