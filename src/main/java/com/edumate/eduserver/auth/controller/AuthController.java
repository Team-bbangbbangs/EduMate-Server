package com.edumate.eduserver.auth.controller;

import com.edumate.eduserver.auth.controller.request.MemberLoginRequest;
import com.edumate.eduserver.auth.controller.request.MemberSignUpRequest;
import com.edumate.eduserver.auth.controller.request.UpdatePasswordRequest;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.auth.facade.response.MemberLoginResponse;
import com.edumate.eduserver.auth.facade.response.MemberReissueResponse;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.RefreshTokenCookieHandler;
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

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;
    private final RefreshTokenCookieHandler refreshTokenCookieHandler;

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

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
        refreshTokenCookieHandler.setRefreshTokenCookie(response, signUpResponse.refreshToken());
        return ApiResponse.success(CommonSuccessCode.OK, signUpResponse);
    }

    @PostMapping("/login")
    public ApiResponse<MemberLoginResponse> login(@RequestBody @Valid final MemberLoginRequest request,
                                                  final HttpServletResponse response) {
        MemberLoginResponse loginResponse = authFacade.login(request.email().strip(), request.password().strip());
        refreshTokenCookieHandler.setRefreshTokenCookie(response, loginResponse.refreshToken());
        return ApiResponse.success(CommonSuccessCode.OK, loginResponse);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@MemberId final long memberId, final HttpServletResponse response) {
        authFacade.logout(memberId);
        refreshTokenCookieHandler.clearRefreshTokenCookie(response);
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PatchMapping("/reissue")
    public ApiResponse<MemberReissueResponse> reissue(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) final String refreshToken,
            final HttpServletResponse response) {
        MemberReissueResponse reissueResponse = authFacade.reissue(refreshToken.strip());
        refreshTokenCookieHandler.setRefreshTokenCookie(response, reissueResponse.refreshToken());
        return ApiResponse.success(CommonSuccessCode.OK, reissueResponse);
    }

    @PatchMapping("/password")
    public ApiResponse<Void> updatePassword(@RequestBody @Valid final UpdatePasswordRequest request) {
        authFacade.updatePassword(request.email().strip(), request.password().strip());
        return ApiResponse.success(CommonSuccessCode.OK);
    }
}
