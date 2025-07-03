package com.edumate.eduserver.auth.controller;

import com.edumate.eduserver.auth.controller.request.MemberLoginRequest;
import com.edumate.eduserver.auth.controller.request.MemberSignUpRequest;
import com.edumate.eduserver.auth.controller.request.PasswordFindRequest;
import com.edumate.eduserver.auth.controller.request.UpdatePasswordRequest;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.auth.facade.response.MemberLoginResponse;
import com.edumate.eduserver.auth.facade.response.MemberReissueResponse;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.CookieHandler;
import com.edumate.eduserver.common.annotation.MemberId;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;
    private final CookieHandler cookieHandler;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/email/send-verification")
    public ApiResponse<Void> sendVerificationEmail(@MemberId final long memberId) {
        authFacade.sendVerificationEmail(memberId);
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/verify-email")
    public ApiResponse<Void> verifyEmail(@RequestParam("id") final String memberUuid,
                                         @RequestParam("code") final String verificationCode) {
        authFacade.verifyEmailCode(memberUuid.strip(), verificationCode.strip());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PostMapping("/signup")
    public ApiResponse<MemberSignUpResponse> signUp(@RequestBody @Valid final MemberSignUpRequest request,
                                                    final HttpServletResponse response) {
        MemberSignUpResponse signUpResponse = authFacade.signUp(request.email().strip(), request.password().strip(),
                request.subject().strip(), request.school().strip());
        cookieHandler.setRefreshTokenCookie(response, signUpResponse.refreshToken());
        log.info("회원가입 성공: email={}", request.email().strip());
        return ApiResponse.success(CommonSuccessCode.OK, signUpResponse);
    }

    @PostMapping("/login")
    public ApiResponse<MemberLoginResponse> login(@RequestBody @Valid final MemberLoginRequest request,
                                                  final HttpServletResponse response) {
        MemberLoginResponse loginResponse = authFacade.login(request.email().strip(), request.password().strip());
        cookieHandler.setRefreshTokenCookie(response, loginResponse.refreshToken());
        log.info("로그인 성공: email={}", request.email().strip());
        return ApiResponse.success(CommonSuccessCode.OK, loginResponse);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@MemberId final long memberId, final HttpServletResponse response) {
        authFacade.logout(memberId);
        cookieHandler.clearRefreshTokenCookie(response);
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PatchMapping("/reissue")
    public ApiResponse<MemberReissueResponse> reissue(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) final String refreshToken,
            final HttpServletResponse response) {
        MemberReissueResponse reissueResponse = authFacade.reissue(refreshToken.strip());
        cookieHandler.setRefreshTokenCookie(response, reissueResponse.refreshToken());
        log.info("토큰 재발급 성공: refreshToken(앞 8자리)={}", refreshToken != null && refreshToken.length() > 8 ? refreshToken.substring(0, 8) : refreshToken);
        return ApiResponse.success(CommonSuccessCode.OK, reissueResponse);
    }

    @PatchMapping("/password")
    public ApiResponse<Void> updatePassword(@RequestBody @Valid final UpdatePasswordRequest request) {
        authFacade.updatePassword(request.email().strip(), request.password().strip());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PostMapping("/find-password")
    public ApiResponse<Void> findPassword(@RequestBody @Valid final PasswordFindRequest request) {
        authFacade.findPassword(request.email().strip());
        return ApiResponse.success(CommonSuccessCode.OK);
    }
}
