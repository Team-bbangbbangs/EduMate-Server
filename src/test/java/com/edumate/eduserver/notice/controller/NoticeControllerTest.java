package com.edumate.eduserver.notice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.exception.NoticeNotFoundException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;
import com.edumate.eduserver.notice.facade.NoticeFacade;
import com.edumate.eduserver.notice.facade.response.NoticeGetResponse;
import com.edumate.eduserver.notice.facade.response.NoticeResponse;
import com.edumate.eduserver.notice.facade.response.NoticesGetResponse;
import com.edumate.eduserver.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(NoticeController.class)
class NoticeControllerTest extends ControllerTest {

    @MockitoBean
    private NoticeFacade noticeFacade;

    private final String BASE_URL = "/api/v1/notices";
    private final String BASE_DOMAIN_PACKAGE = "notice/";

    @ParameterizedTest(name = "[{index}] categoryId={0}, page={1}")
    @CsvSource({
            "2, 1",    // 정상
            "'', 1",   // categoryId 미전달
            "2, ''",   // page 미전달
            "'', ''"   // 둘 다 미전달
    })
    @DisplayName("공지사항 목록을 성공적으로 조회한다.")
    void getNotices_Success(String categoryIdStr, String pageStr) throws Exception {
        Integer categoryId = categoryIdStr.isBlank() ? null : Integer.valueOf(categoryIdStr);
        Integer page = pageStr.isBlank() ? null : Integer.valueOf(pageStr);

        //given
        NoticeCategory noticeCategory = NoticeCategory.NOTICE;
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 15, 30, 45);
        int totalPages = 1;

        NoticeResponse olderNotice = NoticeResponse.of(
                1L,
                noticeCategory.getText(),
                "테스트 공지 #1",
                createdAt.minusHours(1)
        );
        NoticeResponse newerNotice = NoticeResponse.of(
                2L,
                noticeCategory.getText(),
                "테스트 공지 #2",
                createdAt
        );
        List<NoticeResponse> noticeResponses = List.of(newerNotice, olderNotice);
        NoticesGetResponse noticesResponse = new NoticesGetResponse(
                totalPages,
                noticeResponses
        );

        when(noticeFacade.getNotices(any(NoticeCategory.class), anyInt()))
                .thenReturn(noticesResponse);

        //when & then
        MockHttpServletRequestBuilder req = get(BASE_URL);
        if (categoryId != null) {
            req.param("categoryId", String.valueOf(categoryId));
        }
        if (page != null) {
            req.param("page", String.valueOf(page));
        }
        mockMvc.perform(req)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.totalPages").value(totalPages))
                .andExpect(jsonPath("$.data.notices.length()").value(noticeResponses.size()))
                .andExpect(jsonPath("$.data.notices[0].noticeId").value(newerNotice.noticeId()))
                .andExpect(jsonPath("$.data.notices[1].noticeId").value(olderNotice.noticeId()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "get-success",
                        queryParameters(
                                parameterWithName("categoryId").optional().description("공지 카테고리 ID"),
                                parameterWithName("page").optional().description("페이지 번호 (1부터 시작)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.totalPages").description("전체 페이지 수"),
                                fieldWithPath("data.notices[].noticeId").description("공지사항 ID"),
                                fieldWithPath("data.notices[].category").description("공지사항 카테고리"),
                                fieldWithPath("data.notices[].title").description("공지사항 제목"),
                                fieldWithPath("data.notices[].createdAt").description("공지사항 생성일")
                        )
                ));
    }

    @Test
    @DisplayName("공지사항 상세를 성공적으로 조회한다.")
    void getNotice_Success() throws Exception {
        // given
        long noticeId = 1L;
        String category = NoticeCategory.NOTICE.getText();
        String title    = "테스트 공지 제목";
        String content  = "여기는 공지 상세 내용입니다.";
        LocalDateTime createdAt = LocalDateTime.now();

        NoticeGetResponse noticeGetResponse = new NoticeGetResponse(
                noticeId, category, title, content, createdAt
        );

        when(noticeFacade.getNotice(noticeId))
                .thenReturn(noticeGetResponse);

        // when & then
        mockMvc.perform(get(BASE_URL + "/{noticeId}", noticeId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.noticeId").value(noticeId))
                .andExpect(jsonPath("$.data.category").value(category))
                .andExpect(jsonPath("$.data.title").value(title))
                .andExpect(jsonPath("$.data.content").value(content))
                .andExpect(jsonPath("$.data.createdAt").value(createdAt.toString()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "get-one-success",
                        pathParameters(
                                parameterWithName("noticeId").description("공지사항 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.noticeId").description("공지사항 ID"),
                                fieldWithPath("data.category").description("공지사항 카테고리"),
                                fieldWithPath("data.title").description("공지사항 제목"),
                                fieldWithPath("data.content").description("공지사항 내용"),
                                fieldWithPath("data.createdAt").description("공지사항 생성일")
                        )
                ));
    }

    @Test
    @DisplayName("존재하지 않는 공지사항 ID로 조회하면 404 에러를 반환한다.")
    void getNotice_NotFound() throws Exception {
        // given
        long noticeId = 999L;
        NoticeErrorCode noticeNotFoundErrorCode = NoticeErrorCode.NOTICE_NOT_FOUND;
        doThrow(new NoticeNotFoundException(noticeNotFoundErrorCode))
                .when(noticeFacade).getNotice(noticeId);

        // when & then
        mockMvc.perform(get(BASE_URL + "/{noticeId}", noticeId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(noticeNotFoundErrorCode.getCode()))
                .andExpect(jsonPath("$.message").value(noticeNotFoundErrorCode.getMessage()))
                .andDo(CustomRestDocsUtils.documents(
                        BASE_DOMAIN_PACKAGE + "get-one-fail-not-found",
                        pathParameters(
                                parameterWithName("noticeId").description("공지사항 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }
}
