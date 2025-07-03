package com.edumate.eduserver.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.edumate.eduserver.auth.exception.InvalidPasswordLengthException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.docs.CustomRestDocsUtils;
import com.edumate.eduserver.member.controller.request.MemberEmailUpdateRequest;
import com.edumate.eduserver.member.controller.request.MemberProfileUpdateRequest;
import com.edumate.eduserver.member.controller.request.PasswordChangeRequest;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.exception.InvalidPasswordException;
import com.edumate.eduserver.member.exception.MemberNicknameDuplicateException;
import com.edumate.eduserver.member.exception.MemberNicknameInvalidException;
import com.edumate.eduserver.member.exception.PasswordSameAsOldException;
import com.edumate.eduserver.member.exception.code.MemberErrorCode;
import com.edumate.eduserver.member.facade.MemberFacade;
import com.edumate.eduserver.member.facade.response.MemberNicknameValidationResponse;
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
        String school = School.HIGH_SCHOOL.getName();
        String nickname = "선생님";
        MemberProfileGetResponse response = new MemberProfileGetResponse(
                email, subject, isTeacherVerified, school, nickname
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
                .andExpect(jsonPath("$.data.nickname").value(nickname))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "get-profile-success",
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.subject").description("과목명"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.isTeacherVerified").description("교사 인증 여부"),
                                fieldWithPath("data.school").description("학교"),
                                fieldWithPath("data.nickname").description("닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호를 성공적으로 변경한다.")
    void updatePasswordSuccess() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest("currentPw123", "newPw456");

        mockMvc.perform(patch(BASE_URL + "/password")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-password-success",
                        requestFields(
                                fieldWithPath("currentPassword").description("현재 비밀번호"),
                                fieldWithPath("newPassword").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("현재 비밀번호가 일치하지 않으면 400 에러를 반환한다.")
    void updatePasswordFail_wrongCurrentPassword() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest("wrongCurrent", "newPw456");
        doThrow(new InvalidPasswordException(MemberErrorCode.INVALID_CURRENT_PASSWORD)).when(memberFacade)
                .updatePassword(anyLong(), any(), any());

        mockMvc.perform(patch(BASE_URL + "/password")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(MemberErrorCode.INVALID_CURRENT_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(MemberErrorCode.INVALID_CURRENT_PASSWORD.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-password-fail/unmatched-current-password",
                        requestFields(
                                fieldWithPath("currentPassword").description("현재 비밀번호"),
                                fieldWithPath("newPassword").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("새 비밀번호가 이전 비밀번호와 일치하면 400 에러를 반환한다")
    void updatePasswordFail_invalidNewPassword() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest("currentPw123", "short");
        doThrow(new PasswordSameAsOldException(MemberErrorCode.SAME_PASSWORD)).when(memberFacade)
                .updatePassword(anyLong(), any(), any());

        mockMvc.perform(patch(BASE_URL + "/password")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(MemberErrorCode.SAME_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(MemberErrorCode.SAME_PASSWORD.getMessage()))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-password-fail/same-password",
                        requestFields(
                                fieldWithPath("currentPassword").description("현재 비밀번호"),
                                fieldWithPath("newPassword").description("새 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("새 비밀번호 형식이 올바르지 않으면 400 에러를 반환한다")
    void updatePasswordFail_invalidPasswordFormat() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest("currentPw123", "123");
        doThrow(new InvalidPasswordLengthException(AuthErrorCode.INVALID_PASSWORD_LENGTH)).when(memberFacade)
                .updatePassword(anyLong(), any(), any());

        mockMvc.perform(patch(BASE_URL + "/password")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(AuthErrorCode.INVALID_PASSWORD_LENGTH.getCode()))
                .andExpect(jsonPath("$.message").value(AuthErrorCode.INVALID_PASSWORD_LENGTH.getMessage()));
    }

    @Test
    @DisplayName("사용자 프로필을 성공적으로 수정한다.")
    void updateMemberProfile() throws Exception {
        // given
        MemberProfileUpdateRequest request = new MemberProfileUpdateRequest("수학", "MIDDLE", "유태근");
        doNothing().when(memberFacade).updateMemberProfile(1L, "수학", School.MIDDLE_SCHOOL, "유태근");

        // when & then
        mockMvc.perform(patch(BASE_URL + "/profile")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-profile-success",
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("subject").description("과목"),
                                fieldWithPath("school").description("학교"),
                                fieldWithPath("nickname").description("닉네임")

                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("유효하지 않은 닉네임으로 사용자 프로필 수정 시 실패한다.")
    void updateProfileFailWithInvalidNickname() throws Exception {
        // given
        String nickname = "관리자";
        MemberProfileUpdateRequest request = new MemberProfileUpdateRequest("수학", "MIDDLE", nickname);

        doThrow(new MemberNicknameInvalidException(MemberErrorCode.INVALID_NICKNAME, nickname))
                .when(memberFacade)
                .updateMemberProfile(anyLong(), anyString(), any(), anyString());

        // when & then
        mockMvc.perform(patch(BASE_URL + "/profile")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-4000404"))
                .andExpect(jsonPath("$.message").value("입력하신 관리자은(는) 유효하지 않은 닉네임입니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-profile-fail/invalid-nickname",
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("subject").description("과목"),
                                fieldWithPath("school").description("학교"),
                                fieldWithPath("nickname").description("유효하지 않은 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("중복된 닉네임으로 사용자 프로필 수정 시 실패한다.")
    void updateProfileFailWithDuplicatedNickname() throws Exception {
        // given
        String nickname = "중복닉네임";
        MemberProfileUpdateRequest request = new MemberProfileUpdateRequest("수학", "MIDDLE", nickname);

        doThrow(new MemberNicknameDuplicateException(MemberErrorCode.DUPLICATED_NICKNAME, nickname))
                .when(memberFacade)
                .updateMemberProfile(anyLong(), anyString(), any(), anyString());

        // when & then
        mockMvc.perform(patch(BASE_URL + "/profile")

                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("EDMT-4000405"))
                .andExpect(jsonPath("$.message").value("입력하신 중복닉네임은(는) 중복된 닉네임입니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-profile-fail/duplicated-nickname",
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("subject").description("과목"),
                                fieldWithPath("school").description("학교"),
                                fieldWithPath("nickname").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("닉네임 유효성 검사를 성공적으로 수행한다.")
    void validateNickname() throws Exception {
        // given
        MemberNicknameValidationResponse response = new MemberNicknameValidationResponse(false, false);
        when(memberFacade.validateNickname(anyLong(), anyString())).thenReturn(response);

        // when & then
        mockMvc.perform(get(BASE_URL + "/nickname")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .param("nickname", "nickname"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andExpect(jsonPath("$.data.isInvalid").isBoolean())
                .andExpect(jsonPath("$.data.isDuplicated").isBoolean())
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "validate-nickname",
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        queryParameters(
                                parameterWithName("nickname").description("확인할 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.isInvalid").description("유효하지 않은 닉네임 여부"),
                                fieldWithPath("data.isDuplicated").description("중복된 닉네임 여부")
                        )
                ));
    }

    @Test
    @DisplayName("이메일을 성공적으로 수정한다.")
    void updateEmailSuccess() throws Exception {
        // given
        MemberEmailUpdateRequest request = new MemberEmailUpdateRequest("newemail@test.com");
        doNothing().when(memberFacade).updateEmail(anyLong(), anyString());

        // when & then
        mockMvc.perform(patch(BASE_URL + "/email")
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("EDMT-200"))
                .andExpect(jsonPath("$.message").value("요청이 성공했습니다."))
                .andDo(CustomRestDocsUtils.documents(BASE_DOMAIN_PACKAGE + "update-email-success",
                        requestHeaders(
                                headerWithName("Authorization").description("액세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("email").description("새 이메일 주소")
                        ),
                        responseFields(
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지")
                        )
                ));
    }
}
