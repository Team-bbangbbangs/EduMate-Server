package com.edumate.eduserver.notice.domain;

import com.edumate.eduserver.notice.exception.InvalidNoticeCategoryException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeCategory {
    ALL(1, "전체"),
    NOTICE(2, "공지"),
    EVENT(3, "이벤트");

    private final int id;
    private final String text;

    public static NoticeCategory fromId(final Integer code) {
        if (code == null) {
            return ALL;
        }
        return Arrays.stream(NoticeCategory.values())
                .filter(category -> category.id == code)
                .findFirst()
                .orElseThrow(() -> new InvalidNoticeCategoryException(NoticeErrorCode.INVALID_NOTICE_CATEGORY));
    }
}
