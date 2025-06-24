package com.edumate.eduserver.notice.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.notice.controller.request.NoticeCreateRequest;
import com.edumate.eduserver.notice.controller.request.NoticeUpdateRequest;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.facade.NoticeFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeFacade noticeFacade;

    @PostMapping
    public ApiResponse<Void> createNotice(
            @RequestBody @Valid final NoticeCreateRequest request
    ) {
        NoticeCategory category = NoticeCategory.fromId(request.categoryId());
        noticeFacade.createNotice(category, request.title(), request.content());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PatchMapping("/{noticeId}")
    public ApiResponse<Void> updateNotice(
            @RequestBody @Valid final NoticeUpdateRequest request,
            @PathVariable final long noticeId
    ) {
        NoticeCategory category = NoticeCategory.fromId(request.categoryId());
        noticeFacade.updateNotice(noticeId, category, request.title(), request.content());
        return ApiResponse.success(CommonSuccessCode.OK);
    }
}
