package com.edumate.eduserver.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.exception.NoticeNotFoundException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;
import com.edumate.eduserver.notice.repository.NoticeRepository;
import com.edumate.eduserver.util.ServiceTest;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@DisplayName("공지사항 서비스 테스트")
class NoticeServiceTest extends ServiceTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("모든 카테고리 조회 시 전체 공지를 생성일 내림차순으로 반환한다.")
    void getNotices_AllCategory_Success() {
        // given
        Notice older = Notice.builder()
                .category(NoticeCategory.NOTICE)
                .title("오래된 공지")
                .content("내용1")
                .build();
        Notice newer = Notice.builder()
                .category(NoticeCategory.NOTICE)
                .title("최신 공지")
                .content("내용2")
                .build();
        List<Notice> saved = noticeRepository.saveAll(List.of(older, newer));
        Notice savedOlder = saved.get(0);
        Notice savedNewer = saved.get(1);

        // when
        Page<Notice> result = noticeService.getNotices(null, 0);

        // then
        assertThat(result.getContent())
                .extracting(Notice::getId)
                .containsExactly(savedNewer.getId(), savedOlder.getId());
    }

    @Test
    @DisplayName("특정 카테고리 조회 시 해당 카테고리 공지만 생성일 내림차순으로 반환한다.")
    void getNotices_ByCategory_Success() {
        // given
        Notice eventOld = Notice.builder()
                .category(NoticeCategory.EVENT)
                .title("이벤트 공지1")
                .content("내용A")
                .build();
        Notice eventNew = Notice.builder()
                .category(NoticeCategory.EVENT)
                .title("이벤트 공지2")
                .content("내용B")
                .build();
        Notice other = Notice.builder()
                .category(NoticeCategory.NOTICE)
                .title("일반 공지")
                .content("내용C")
                .build();
        List<Notice> saved = noticeRepository.saveAll(List.of(eventOld, eventNew, other));
        Notice savedEventOld = saved.get(0);
        Notice savedEventNew = saved.get(1);

        // when
        Page<Notice> result = noticeService.getNotices(NoticeCategory.EVENT, 0);

        // then
        assertThat(result.getContent())
                .extracting(Notice::getId)
                .containsExactly(savedEventNew.getId(), savedEventOld.getId());
    }

    @Test
    @DisplayName("존재하지 않는 공지사항 ID 조회 시 예외를 발생시킨다.")
    void getNotice_NotFound() {
        // given
        long missingId = 999L;

        // when & then
        assertThatThrownBy(() -> noticeService.getNotice(missingId))
                .isInstanceOf(NoticeNotFoundException.class)
                .hasMessage(NoticeErrorCode.NOTICE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("공지사항이 10개 초과 저장된 경우 두번째 페이지를 조회하면 남은 공지 하나만 반환한다.")
    void getNotices_SecondPage_Success() {
        // given
        List<Notice> allNotices = IntStream.rangeClosed(1, 11)
                .mapToObj(i -> Notice.builder()
                        .category(NoticeCategory.NOTICE)
                        .title("공지 " + i)
                        .content("내용 " + i)
                        .build())
                .toList();
        List<Notice> savedAll = noticeRepository.saveAll(allNotices);
        Notice oldest = savedAll.get(0);

        // when
        Page<Notice> secondPage = noticeService.getNotices(null, 1);

        // then
        assertThat(secondPage.getContent())
                .extracting(Notice::getId)
                .containsExactly(oldest.getId());
    }
}
