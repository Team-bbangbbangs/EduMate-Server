package com.edumate.eduserver.notice.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.edumate.eduserver.notice.domain.Notice;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.facade.response.NoticeGetResponse;
import com.edumate.eduserver.notice.facade.response.NoticesGetResponse;
import com.edumate.eduserver.notice.service.NoticeService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

class NoticeFacadeTest {

    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeFacade noticeFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("공지사항 목록 조회가 정상 동작한다.")
    void getNotices() {
        // given
        NoticeCategory category = NoticeCategory.NOTICE;
        int page = 1;
        int zeroBasedPage = page - 1;

        Notice notice1 = mock(Notice.class);
        Notice notice2 = mock(Notice.class);

        given(notice1.getId()).willReturn(1L);
        given(notice1.getCategory()).willReturn(category);
        given(notice1.getTitle()).willReturn("공지 제목1");
        given(notice1.getContent()).willReturn("공지 내용1");
        given(notice1.getCreatedAt()).willReturn(LocalDateTime.of(2025, 6, 1, 9, 0));

        given(notice2.getId()).willReturn(2L);
        given(notice2.getCategory()).willReturn(category);
        given(notice2.getTitle()).willReturn("공지 제목2");
        given(notice2.getContent()).willReturn("공지 내용2");
        given(notice2.getCreatedAt()).willReturn(LocalDateTime.of(2025, 6, 2, 10, 0));

        Page<Notice> noticePage = new PageImpl<>(List.of(notice1, notice2));
        given(noticeService.getNotices(category, zeroBasedPage)).willReturn(noticePage);

        // when
        NoticesGetResponse response = noticeFacade.getNotices(category, page);

        // then
        assertThat(response).isNotNull();
        assertThat(response.notices()).hasSize(2);
        assertThat(response.totalPages()).isEqualTo(noticePage.getTotalPages());

        verify(noticeService).getNotices(category, zeroBasedPage);
    }

    @Test
    @DisplayName("공지사항 상세 조회가 정상 동작한다.")
    void getNotice() {
        // given
        long noticeId = 1L;
        NoticeCategory noticeCategory = NoticeCategory.NOTICE;
        String title = "공지사항 제목";
        String content = "공지사항 내용";

        Notice notice = mock(Notice.class);
        given(notice.getId()).willReturn(noticeId);
        given(notice.getCategory()).willReturn(noticeCategory);
        given(notice.getTitle()).willReturn(title);
        given(notice.getContent()).willReturn(content);
        given(notice.getCreatedAt()).willReturn(LocalDateTime.of(2025, 6, 1, 10, 30));
        given(noticeService.getNotice(noticeId)).willReturn(notice);

        // when
        NoticeGetResponse response = noticeFacade.getNotice(noticeId);

        // then
        assertThat(response.noticeId()).isEqualTo(noticeId);
        assertThat(response.category()).isEqualTo(noticeCategory.getText());
        assertThat(response.title()).isEqualTo(notice.getTitle());
        assertThat(response.content()).isEqualTo(notice.getContent());

        verify(noticeService).getNotice(noticeId);
    }

    @Test
    @DisplayName("공지사항 생성이 정상 동작한다.")
    void createNotice_Success() {
        // given
        NoticeCategory category = NoticeCategory.NOTICE;
        String title = "테스트 공지 제목";
        String content = "테스트 공지 내용";

        willDoNothing().given(noticeService).createNotice(category, title, content);

        // when
        noticeFacade.createNotice(category, title, content);

        // then
        verify(noticeService).createNotice(category, title, content);
    }

    @Test
    @DisplayName("공지사항 수정이 정상 동작한다.")
    void updateNotice_Success() {
        // given
        long noticeId = 1L;
        NoticeCategory category = NoticeCategory.NOTICE;
        String title = "수정된 제목";
        String content = "수정된 내용";

        willDoNothing().given(noticeService).updateNotice(noticeId, category, title, content);

        // when
        noticeFacade.updateNotice(noticeId, category, title, content);

        // then
        verify(noticeService).updateNotice(noticeId, category, title, content);
    }

    @Test
    @DisplayName("공지사항 삭제가 정상 동작한다.")
    void deleteNotice_Success() {
        // given
        long noticeId = 1L;
        willDoNothing().given(noticeService).deleteNotice(noticeId);

        // when
        noticeFacade.deleteNotice(noticeId);

        // then
        verify(noticeService).deleteNotice(noticeId);
    }

}
