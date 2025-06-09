package com.edumate.eduserver.notice.facade;

import com.edumate.eduserver.notice.facade.response.NoticeGetResponse;
import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.facade.response.NoticesGetResponse;
import com.edumate.eduserver.notice.service.NoticeService;
import com.edumate.eduserver.notice.service.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeFacade {

    private final NoticeService noticeService;

    public NoticesGetResponse getNotices(final NoticeCategory category, final int page) {
        int zeroBasedPage = page - 1;
        Page<Notice> noticePages = noticeService.getNotices(category, zeroBasedPage);
        return NoticesGetResponse.of(
                noticePages.getTotalPages(),
                noticePages.stream().map(NoticeDto::of).toList()
        );
    }

    public NoticeGetResponse getNotice(long noticeId) {
        NoticeDto noticeDto = noticeService.getNotice(noticeId);
        return NoticeGetResponse.of(noticeDto);
    }
}
