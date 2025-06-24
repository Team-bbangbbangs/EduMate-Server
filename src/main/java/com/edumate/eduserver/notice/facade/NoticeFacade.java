package com.edumate.eduserver.notice.facade;

import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.facade.response.NoticeGetResponse;
import com.edumate.eduserver.notice.facade.response.NoticeResponse;
import com.edumate.eduserver.notice.facade.response.NoticesGetResponse;
import com.edumate.eduserver.notice.service.NoticeService;
import java.util.List;
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
        List<NoticeResponse> notices = noticePages.getContent().stream()
                .map(notice -> new NoticeResponse(notice.getId(), notice.getCategory().getText(), notice.getTitle(),
                        notice.getCreatedAt())).toList();
        return NoticesGetResponse.of(
                noticePages.getTotalPages(),
                notices
        );
    }

    public NoticeGetResponse getNotice(final long noticeId) {
        Notice notice = noticeService.getNotice(noticeId);
        return NoticeGetResponse.of(
                notice.getId(),
                notice.getCategory().getText(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt()
        );
    }

    @Transactional
    public void createNotice(final NoticeCategory category, final String title, final String content) {
        noticeService.createNotice(category, title, content);
    }

    @Transactional
    public void updateNotice(final long noticeId, final NoticeCategory category, final String title, final String content) {
        noticeService.updateNotice(noticeId, category, title, content);
    }

    @Transactional
    public void deleteNotice(long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}
