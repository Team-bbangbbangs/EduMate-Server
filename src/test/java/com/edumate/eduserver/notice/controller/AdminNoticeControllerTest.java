package com.edumate.eduserver.notice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.notice.controller.request.NoticeCreateRequest;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.exception.InvalidNoticeCategoryException;
import com.edumate.eduserver.notice.exception.code.NoticeErrorCode;
import com.edumate.eduserver.notice.facade.NoticeFacade;
import com.edumate.eduserver.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(AdminNoticeController.class)
class AdminNoticeControllerTest extends ControllerTest {

    @MockitoBean
    private NoticeFacade noticeFacade;

    private final String BASE_URL = "/api/v1/admin/notices";
    private final String BASE_DOMAIN_PACKAGE = "notice/";
    private static final String ACCESS_TOKEN = "access-token";

    @Test
    @DisplayName("공지사항을 성공적으로 작성한다.")
    void createNotice_Success() throws Exception {
        // given
        NoticeCreateRequest request = new NoticeCreateRequest(
                NoticeCategory.NOTICE.getId(),
                "공지 제목입니다",
                "공지 내용입니다"
        );

        doNothing().when(noticeFacade).createNotice(any(), any(), any());

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType("application/json")
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(
                        BASE_DOMAIN_PACKAGE + "create-success",
                        requestFields(
                                fieldWithPath("categoryId").description("공지사항 카테고리 ID"),
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("공지사항 제목이 null인 경우 400 에러가 발생한다.")
    void createNotice_Fail_TitleNull() throws Exception {
        // given
        NoticeCreateRequest request = new NoticeCreateRequest(
                NoticeCategory.NOTICE.getId(),
                null,
                "공지 내용입니다"
        );

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType("application/json")
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("공지사항 카테고리가 ALL인 경우 400 에러가 발생한다.")
    void createNotice_Fail_InvalidCategory() throws Exception {
        // given
        NoticeCreateRequest request = new NoticeCreateRequest(
                NoticeCategory.ALL.getId(),
                "공지 제목입니다",
                "공지 내용입니다"
        );
        doThrow(new InvalidNoticeCategoryException(NoticeErrorCode.UNWRITABLE_NOTICE_CATEGORY))
                .when(noticeFacade).createNotice(any(), any(), any());

        // when & then
        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType("application/json")
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(NoticeErrorCode.UNWRITABLE_NOTICE_CATEGORY.getCode()))
                .andExpect(jsonPath("$.message").value(NoticeErrorCode.UNWRITABLE_NOTICE_CATEGORY.getMessage()))
                .andDo(CustomRestDocsUtils.documents(
                        BASE_DOMAIN_PACKAGE + "create-fail-invalid-category",
                        requestFields(
                                fieldWithPath("categoryId").description("공지사항 카테고리 ID"),
                                fieldWithPath("title").description("공지사항 제목"),
                                fieldWithPath("content").description("공지사항 내용")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }
}
