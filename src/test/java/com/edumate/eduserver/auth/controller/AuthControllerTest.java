package com.edumate.eduserver.auth.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.auth.controller.request.MemberLoginRequest;
import com.edumate.eduserver.auth.controller.request.MemberSignUpRequest;
import com.edumate.eduserver.auth.controller.request.UpdatePasswordRequest;
import com.edumate.eduserver.auth.exception.AuthCodeNotFoundException;
import com.edumate.eduserver.auth.exception.ExpiredCodeException;
import com.edumate.eduserver.auth.exception.ExpiredTokenException;
import com.edumate.eduserver.auth.exception.InvalidPasswordFormatException;
import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import com.edumate.eduserver.auth.exception.MemberAlreadyRegisteredException;
import com.edumate.eduserver.auth.exception.MisMatchedCodeException;
import com.edumate.eduserver.auth.exception.MismatchedPasswordException;
import com.edumate.eduserver.auth.exception.MismatchedTokenException;
import com.edumate.eduserver.auth.exception.PasswordSameAsOldException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.auth.facade.response.MemberLoginResponse;
import com.edumate.eduserver.auth.facade.response.MemberReissueResponse;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.member.exception.MemberNotFoundException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
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
                                fieldWithPath("status").description("HTTP 상태 ���드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUpSuccess() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest(
                "test@email.com", "password123", "수학", "middle");
        MemberSignUpResponse response = new MemberSignUpResponse("access-token", "refresh-token");

        when(authFacade.signUp(
                request.email().trim(), request.password().trim(), request.subject().trim(), request.school().trim()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "signup-success",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("회원 비밀번호"),
                                fieldWithPath("subject").description("회원이 가르치는 과목"),
                                fieldWithPath("school").description("중학교/고등학교")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.accessToken").description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰")
                        )
                ));
    }


    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시 예외가 발생한다.")
    void signUpConflict() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest(
                "duplicate@email.com", "password123", "수학", "middle");
        doThrow(new MemberAlreadyRegisteredException(AuthErrorCode.MEMBER_ALREADY_REGISTERED))
                .when(authFacade).signUp(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()
                );

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.MEMBER_ALREADY_REGISTERED.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.MEMBER_ALREADY_REGISTERED.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "signup-fail/duplicate-email",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 길이 오류로 회원가입 시 예외가 발생한다.")
    void signUpInvalidPasswordLength() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest("test@email.com", "short", "수학", "middle");
        doThrow(new com.edumate.eduserver.auth.exception.InvalidPasswordLengthException(
                AuthErrorCode.INVALID_PASSWORD_LENGTH))
                .when(authFacade).signUp(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()
                );

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_PASSWORD_LENGTH.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PASSWORD_LENGTH.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "signup-fail/invalid-password-length",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 형식 오류(영문, 숫자, 특수문자 미포함)로 회원가입 시 예외가 발생한다.")
    void signUpInvalidPasswordFormat() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest("test@email.com", "password", "수학", "middle");
        doThrow(new com.edumate.eduserver.auth.exception.InvalidPasswordFormatException(
                AuthErrorCode.INVALID_PASSWORD_FORMAT))
                .when(authFacade).signUp(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()
                );

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_PASSWORD_FORMAT.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PASSWORD_FORMAT.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "signup-fail/invalid-password-format",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 반복 문자 오류로 회원가입 시 예외가 발생한다.")
    void signUpInvalidPasswordRepetition() throws Exception {
        MemberSignUpRequest request = new MemberSignUpRequest("test@email.com", "aaa12345", "수학", "middle");
        doThrow(new com.edumate.eduserver.auth.exception.InvalidPasswordFormatException(
                AuthErrorCode.INVALID_PASSWORD_FORMAT))
                .when(authFacade).signUp(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyString()
                );

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_PASSWORD_FORMAT.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PASSWORD_FORMAT.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "signup-fail/invalid-password-repetition",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    void loginSuccess() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("test@email.com", "password123");
        MemberLoginResponse response = new MemberLoginResponse("access-token", "refresh-token", false);
        when(authFacade.login(request.email().strip(), request.password().strip())).thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "login-success",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("회원 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.accessToken").description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰"),
                                fieldWithPath("data.isAdmin").description("관리자 여부")
                        )
                ));
    }

    @Test
    @DisplayName("로그인에 실패한다 - 잘못된 비밀번호")
    void loginFailWrongPassword() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("test@email.com", "wrongpassword");
        when(authFacade.login(request.email().strip(), request.password().strip()))
                .thenThrow(new MismatchedPasswordException(AuthErrorCode.INVALID_PASSWORD));

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PASSWORD.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "login-fail/invalid-password",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("회원 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("로그인에 실패한다 - 존재하지 않는 이메일")
    void loginFailNotFoundEmail() throws Exception {
        MemberLoginRequest request = new MemberLoginRequest("notfound@email.com", "password123");
        when(authFacade.login(request.email().strip(), request.password().strip()))
                .thenThrow(new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND));

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(MemberErrorCode.MEMBER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(MemberErrorCode.MEMBER_NOT_FOUND.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "login-fail/member-not-found",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("회원 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃에 성공한다.")
    void logoutSuccess() throws Exception {
        doNothing().when(authFacade).logout(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/logout")
                        .header("Authorization", "Bearer access-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "logout-success",
                        requestHeaders(
                                headerWithName("Authorization").description("인증된 액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("토큰 재발급에 성공한다.")
    void reissueSuccess() throws Exception {
        MemberReissueResponse response = new MemberReissueResponse("new-access-token", "new-refresh-token");
        when(authFacade.reissue(anyString())).thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/reissue")
                        .header("Authorization", "Bearer refresh-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("new-refresh-token"))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "reissue-success",
                        requestHeaders(
                                headerWithName("Authorization").description("리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.accessToken").description("새로운 액세스 토큰"),
                                fieldWithPath("data.refreshToken").description("새로운 리프레시 토큰")
                        )
                ));
    }


    @Test
    @DisplayName("토큰 재발급 실패 - 만료된 리프레시 토큰")
    void reissueFailWithExpiredToken() throws Exception {
        when(authFacade.reissue(anyString())).thenThrow(new ExpiredTokenException(AuthErrorCode.EXPIRED_TOKEN));

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/reissue")
                        .header("Authorization", "Bearer expired-refresh-token"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.EXPIRED_TOKEN.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "reissue-fail/expired-token",
                        requestHeaders(
                                headerWithName("Authorization").description("만료된 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 유효하지 않은 리프레시 토큰")
    void reissueFailWithInvalidToken() throws Exception {
        when(authFacade.reissue(anyString())).thenThrow(new MismatchedTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE));

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/reissue")
                        .header("Authorization", "Bearer invalid-refresh-token"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_REFRESH_TOKEN_VALUE.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "reissue-fail/invalid-token",
                        requestHeaders(
                                headerWithName("Authorization").description("데이터베이스에 저장된 토큰과 일치하지 않는 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("이메일 인증 메일 전송에 성공한다.")
    void sendVerificationEmailSuccess() throws Exception {
        doNothing().when(authFacade).sendVerificationEmail(anyLong());

        mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + "/email/send-verification")
                        .header("Authorization", "Bearer access-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "send-email-success",
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
        verify(authFacade).sendVerificationEmail(anyLong());
    }

    @Test
    @DisplayName("비밀번호 변경에 성공한다.")
    void updatePasswordSuccess() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("test@email.com", "newPassword123!");
        doNothing().when(authFacade).updatePassword(request.email().strip(), request.password().strip());

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-password-success",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));

        verify(authFacade).updatePassword(request.email().strip(), request.password().strip());
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 비밀번호 변경 시 예외가 발생한다.")
    void updatePasswordMemberNotFound() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("notfound@email.com", "newPassword123!");
        doThrow(new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND))
                .when(authFacade).updatePassword(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value(MemberErrorCode.MEMBER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(MemberErrorCode.MEMBER_NOT_FOUND.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-password-fail/member-not-found",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 길이 오류로 비밀번호 변경 시 예외가 발생한다.")
    void updatePasswordInvalidPasswordLength() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("test@email.com", "short");
        doThrow(new InvalidPasswordLengthException(AuthErrorCode.INVALID_PASSWORD_LENGTH))
                .when(authFacade).updatePassword(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_PASSWORD_LENGTH.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PASSWORD_LENGTH.getMessage()))
                .andDo(CustomRestDocsUtils.documents(
                        BASE_DOMAIN_PACKAGE + "update-password-fail/invalid-password-length",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 형식 오류로 비밀번호 변경 시 예외가 발생한다.")
    void updatePasswordInvalidPasswordFormat() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("test@email.com", "password");
        doThrow(new InvalidPasswordFormatException(AuthErrorCode.INVALID_PASSWORD_FORMAT))
                .when(authFacade).updatePassword(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_PASSWORD_FORMAT.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PASSWORD_FORMAT.getMessage()))
                .andDo(CustomRestDocsUtils.documents(
                        BASE_DOMAIN_PACKAGE + "update-password-fail/invalid-password-format",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("이전과 동일한 비밀번호 변경 시 예외가 발생한다.")
    void updatePasswordWithSamePassword() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("test@email.com", "password");
        doThrow(new PasswordSameAsOldException(AuthErrorCode.SAME_PASSWORD))
                .when(authFacade).updatePassword(anyString(), anyString());

        mockMvc.perform(RestDocumentationRequestBuilders.patch(BASE_URL + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value(AuthErrorCode.SAME_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.SAME_PASSWORD.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-password-fail/password-repetition",
                        requestFields(
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }
}
