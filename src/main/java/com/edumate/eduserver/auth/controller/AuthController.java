package com.edumate.eduserver.auth.controller;

import com.edumate.eduserver.auth.controller.request.EmailSendRequest;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.auth.facade.response.EmailVerifyResponse;
import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/auth/email/send-verification")
    public ApiResponse<Void> sendVerificationEmail(@RequestBody @Valid final EmailSendRequest request) {
        authFacade.sendVerificationEmail(request.memberUuid());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/auth/email/verify-email")
    public ApiResponse<EmailVerifyResponse> verifyEmail(@RequestParam("id") final String memberUuid,
                                                        @RequestParam("code") final String verificationCode) {
        EmailVerifyResponse response = authFacade.verifyEmailCode(memberUuid.trim(), verificationCode.trim());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }
}
