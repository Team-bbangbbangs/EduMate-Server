package com.edumate.eduserver.auth.controller;

import com.edumate.eduserver.auth.controller.request.EmailSendRequest;
import com.edumate.eduserver.auth.facade.AuthFacade;
import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
