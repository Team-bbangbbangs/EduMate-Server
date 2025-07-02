package com.edumate.eduserver.member.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.annotation.MemberId;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.member.controller.request.PasswordChangeRequest;
import com.edumate.eduserver.member.controller.request.MemberProfileUpdateRequest;
import com.edumate.eduserver.member.domain.School;
import com.edumate.eduserver.member.facade.MemberFacade;
import com.edumate.eduserver.member.facade.response.MemberNicknameValidationResponse;
import com.edumate.eduserver.member.facade.response.MemberProfileGetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberFacade memberFacade;

    @GetMapping("/profile")
    public ApiResponse<MemberProfileGetResponse> getMemberProfile(@MemberId final long memberId) {
        return ApiResponse.success(CommonSuccessCode.OK, memberFacade.getMemberProfile(memberId));
    }

    @PatchMapping("/password")
    public ApiResponse<Void> updatePassword(@MemberId final long memberId,
                                            @RequestBody @Valid final PasswordChangeRequest request) {
        memberFacade.updatePassword(memberId, request.currentPassword(), request.newPassword());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PatchMapping("/profile")
    public ApiResponse<Void> updateMemberProfile(
            @MemberId final long memberId,
            @RequestBody @Valid final MemberProfileUpdateRequest request
    ) {
        School school = School.fromName(request.school());
        memberFacade.updateMemberProfile(memberId, request.subject(), school, request.nickname());

        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/nickname")
    public ApiResponse<MemberNicknameValidationResponse> validateNickname(
            @MemberId final long memberId,
            @RequestParam final String nickname
    ) {
        return ApiResponse.success(CommonSuccessCode.OK, memberFacade.validateNickname(memberId, nickname));
    }
}
