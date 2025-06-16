package com.edumate.eduserver.auth.controller;

import com.edumate.eduserver.auth.controller.request.EmailSendRequest;
import com.edumate.eduserver.auth.controller.request.MemberLoginRequest;
import com.edumate.eduserver.auth.controller.request.MemberSignUpRequest;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.auth.facade.response.MemberLoginResponse;
import com.edumate.eduserver.auth.facade.response.MemberReissueResponse;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.annotation.MemberId;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/email/send-verification")
    public ApiResponse<Void> sendVerificationEmail(@RequestBody @Valid final EmailSendRequest request) {
        authFacade.sendVerificationEmail(request.memberUuid().strip());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/verify-email")
    public ApiResponse<Void> verifyEmail(@RequestParam("id") final String memberUuid,
                                         @RequestParam("code") final String verificationCode) {
        authFacade.verifyEmailCode(memberUuid.strip(), verificationCode.strip());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PostMapping("/signup")
    public ApiResponse<MemberSignUpResponse> signUp(@RequestBody @Valid final MemberSignUpRequest request) {
        MemberSignUpResponse response = authFacade.signUp(request.email().strip(), request.password().strip(),
                request.subject().strip(), request.school().strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @PostMapping("/login")
    public ApiResponse<MemberLoginResponse> login(@RequestBody @Valid final MemberLoginRequest request) {
        MemberLoginResponse response = authFacade.login(request.email().strip(), request.password().strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @PatchMapping("/logout")
    public ApiResponse<Void> logout(@MemberId final long memberId) {
        authFacade.logout(memberId);
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PatchMapping("/reissue")
    public ApiResponse<MemberReissueResponse> reissue(@RequestHeader(HttpHeaders.AUTHORIZATION) final String refreshToken) {
        MemberReissueResponse response = authFacade.reissue(refreshToken.strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }
}
