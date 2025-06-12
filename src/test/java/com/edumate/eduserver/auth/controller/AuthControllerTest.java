package com.edumate.eduserver.auth.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.auth.exception.AuthCodeNotFoundException;
import com.edumate.eduserver.auth.exception.ExpiredCodeException;
import com.edumate.eduserver.auth.exception.MisMatchedCodeException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("인증 컨트롤러 테스트")
@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTest {

    @MockitoBean
    private AuthFacade authFacade;

    private static final String BASE_URL = "/api/v1/auth";
    private final String BASE_DOMAIN_PACKAGE = "auth/";
    private static final String MEMBER_UUID = "test-member-uuid";
    private static final String CODE = "123456";

    @Test
    @DisplayName("이메일 인증을 성공한다.")
    void verifyEmail() throws Exception {
        doNothing().when(authFacade).verifyEmailCode(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/verify-email")
                        .param("id", MEMBER_UUID)
                        .param("code", CODE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "verify-email-success",
                        queryParameters(
                                parameterWithName("id").description("회원 UUID"),
                                parameterWithName("code").description("이메일 인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("만료된 인증 코드로 인증을 시도할 경우 예외가 발생한다.")
    void expiredCode() throws Exception {
        doThrow(new ExpiredCodeException(AuthErrorCode.EXPIRED_CODE))
                .when(authFacade).verifyEmailCode(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/verify-email")
                        .param("id", MEMBER_UUID)
                        .param("code", CODE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.EXPIRED_CODE.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.EXPIRED_CODE.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "verify-email-fail/expired-code",
                        queryParameters(
                                parameterWithName("id").description("회원 UUID"),
                                parameterWithName("code").description("이메일 인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("유효하지 않은 인증 코드로 인증을 시도할 경우 예외가 발생한다.")
    void authCodeNotFound() throws Exception {
        doThrow(new AuthCodeNotFoundException(AuthErrorCode.AUTH_CODE_NOT_FOUND))
                .when(authFacade).verifyEmailCode(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/verify-email")
                        .param("id", MEMBER_UUID)
                        .param("code", CODE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.AUTH_CODE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.AUTH_CODE_NOT_FOUND.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "verify-email-fail/auth-code-not-found",
                        queryParameters(
                                parameterWithName("id").description("회원 UUID"),
                                parameterWithName("code").description("이메일 인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("코드가 일치하지 않을 경우 예외가 발생한다.")
    void misMatchedCode() throws Exception {
        doThrow(new MisMatchedCodeException(AuthErrorCode.INVALID_CODE))
                .when(authFacade).verifyEmailCode(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/verify-email")
                        .param("id", MEMBER_UUID)
                        .param("code", CODE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_CODE.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_CODE.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "verify-email-fail/code-mismatch",
                        queryParameters(
                                parameterWithName("id").description("회원 UUID"),
                                parameterWithName("code").description("이메일 인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }
}
