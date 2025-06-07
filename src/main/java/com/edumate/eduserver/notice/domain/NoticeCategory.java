package com.edumate.eduserver.notice.domain;

import com.edumate.eduserver.notice.exception.NoticeCategoryNotFoundException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;
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

    public static NoticeCategory fromId(Integer code) {
        if (code == null) {
            return ALL;
        }
        for (NoticeCategory category : NoticeCategory.values()) {
            if (category.id == code) {
                return category;
            }
        }
        throw new NoticeCategoryNotFoundException(NoticeErrorCode.INVALID_NOTICE_CATEGORY);
    }
}
