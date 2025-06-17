package com.edumate.eduserver.member.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.facade.MemberFacade;
import com.edumate.eduserver.member.facade.response.MemberProfileGetResponse;
import com.edumate.eduserver.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("멤버 컨트롤러 테스트")
@WebMvcTest(MemberController.class)
class MemberControllerTest extends ControllerTest {

    @MockitoBean
    private MemberFacade memberFacade;

    private static final String BASE_URL = "/api/v1/users";
    private static final String BASE_DOMAIN_PACKAGE = "member/";
    private static final String ACCESS_TOKEN = "access-token";

    @Test
    @DisplayName("멤버 프로필을 성공적으로 조회한다.")
    void getMemberProfileSuccess() throws Exception {
        // given
        String email = "test@email.com";
        String subject = "수학";
        boolean isTeacherVerified = true;
        String school = School.HIGH_SCHOOL.getName().toLowerCase();
        MemberProfileGetResponse response = new MemberProfileGetResponse(
                email,
                subject,
                isTeacherVerified,
                school
        );

        when(memberFacade.getMemberProfile(anyLong()))
                .thenReturn(response);

        // when & then
        mockMvc.perform(get(BASE_URL + "/profile")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.subject").value(subject))
                .andExpect(jsonPath("$.data.isTeacherVerified").value(isTeacherVerified))
                .andExpect(jsonPath("$.data.school").value(school))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "get-profile-success",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.subject").description("과목명"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.isTeacherVerified").description("교사 인증 여부"),
                                fieldWithPath("data.school").description("학교")
                        )
                ));
    }
}
