package com.edumate.eduserver.notice.repository;

import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Notice> findAllByCategoryOrderByCreatedAtDesc(NoticeCategory category, Pageable pageable);
}
