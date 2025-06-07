package com.edumate.eduserver.notice.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.notice.controller.response.NoticesGetResponse;
import com.edumate.eduserver.notice.domain.NoticeCategory;
import com.edumate.eduserver.notice.facade.NoticeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeFacade noticeFacade;

    @GetMapping
    public ApiResponse<NoticesGetResponse> getNotices(
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ) {
        NoticeCategory category = NoticeCategory.fromId(categoryId);
        return ApiResponse.success(
                CommonSuccessCode.OK,
                noticeFacade.getNotices(category, page)
        );
    }
}
