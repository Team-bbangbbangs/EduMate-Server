package com.edumate.eduserver.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.exception.InvalidNoticeCategoryException;
import com.edumate.eduserver.notice.exception.NoticeNotFoundException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;
import com.edumate.eduserver.notice.repository.NoticeRepository;
import com.edumate.eduserver.util.ServiceTest;
import java.util.List;
import java.util.Optional;
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
        Notice older = Notice.create(NoticeCategory.NOTICE, "오래된 공지", "내용1");
        Notice newer = Notice.create(NoticeCategory.NOTICE, "최신 공지", "내용2");
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
        Notice eventOld = Notice.create(NoticeCategory.EVENT, "이벤트 공지1", "내용A");
        Notice eventNew = Notice.create(NoticeCategory.EVENT, "이벤트 공지2", "내용B");
        Notice other = Notice.create(NoticeCategory.NOTICE, "일반 공지", "내용C");
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
                .mapToObj(i -> Notice.create(NoticeCategory.NOTICE, "공지 " + i, "내용 " + i))
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

    @Test
    @DisplayName("공지사항 작성이 정상 동작한다.")
    void createNotice_Success() {
        // given
        NoticeCategory category = NoticeCategory.NOTICE;
        String title = "새 공지사항 제목";
        String content = "새 공지사항 내용";

        // when
        noticeService.createNotice(category, title, content);

        // then
        List<Notice> all = noticeRepository.findAll();
        assertThat(all).hasSize(1);
        Notice saved = all.get(0);
        assertThat(saved.getCategory()).isEqualTo(category);
        assertThat(saved.getTitle()).isEqualTo(title);
        assertThat(saved.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("작성 불가능한 카테고리로 공지사항 작성 시 예외가 발생한다.")
    void createNotice_InvalidCategory_ThrowsException() {
        // given
        NoticeCategory invalidCategory = NoticeCategory.ALL;
        String title = "잘못된 공지";
        String content = "내용";

        // when & then
        assertThatThrownBy(() -> noticeService.createNotice(invalidCategory, title, content))
                .isInstanceOf(InvalidNoticeCategoryException.class)
                .hasMessage(NoticeErrorCode.UNWRITABLE_NOTICE_CATEGORY.getMessage());
    }

    @Test
    @DisplayName("공지사항 수정을 정상적으로 수행한다.")
    void updateNotice_Success() {
        // given
        Notice notice = noticeRepository.save(Notice.create(NoticeCategory.NOTICE, "기존 제목", "기존 내용"));
        String updatedTitle = "수정된 제목";
        String updatedContent = "수정된 내용";
        NoticeCategory updatedCategory = NoticeCategory.EVENT;

        // when
        noticeService.updateNotice(notice.getId(), updatedCategory, updatedTitle, updatedContent);

        // then
        Notice updated = noticeRepository.findById(notice.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo(updatedTitle);
        assertThat(updated.getContent()).isEqualTo(updatedContent);
        assertThat(updated.getCategory()).isEqualTo(updatedCategory);
    }

    @Test
    @DisplayName("공지사항 삭제를 정상적으로 수행한다.")
    void deleteNotice_Success() {
        // given
        Notice notice = noticeRepository.save(Notice.create(NoticeCategory.NOTICE, "삭제할 공지", "내용"));
        long id = notice.getId();

        // when
        noticeService.deleteNotice(id);

        // then
        Optional<Notice> deleteNotice = noticeRepository.findById(id);
        assertThat(deleteNotice).isEmpty();
    }
}
