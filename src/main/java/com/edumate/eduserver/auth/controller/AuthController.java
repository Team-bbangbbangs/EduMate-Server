package com.edumate.eduserver.auth.controller;

import com.edumate.eduserver.auth.controller.request.EmailSendRequest;
import com.edumate.eduserver.auth.controller.request.MemberSignUpRequest;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.auth.facade.response.MemberSignUpResponse;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/email/send-verification")
    public ApiResponse<Void> sendVerificationEmail(@RequestBody @Valid final EmailSendRequest request) {
        authFacade.sendVerificationEmail(request.memberUuid().trim());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/verify-email")
    public ApiResponse<Void> verifyEmail(@RequestParam("id") final String memberUuid,
                                         @RequestParam("code") final String verificationCode) {
        authFacade.verifyEmailCode(memberUuid.trim(), verificationCode.trim());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PostMapping("/signup")
    public ApiResponse<MemberSignUpResponse> signUp(@RequestBody @Valid final MemberSignUpRequest request) {
        MemberSignUpResponse response = authFacade.signUp(request.email().trim(), request.password().trim(),
                request.subject().trim(), request.school().trim());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }
}
