package com.edumate.eduserver.notice.service;

import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private static final int PAGE_SIZE = 10;

    public Page<Notice> getNotices(final NoticeCategory category, final int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        if (category == null || category == NoticeCategory.ALL) {
            return noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
        }
        return noticeRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
    }
}
