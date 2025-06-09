package com.edumate.eduserver.studentrecord.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.StudentRecordFacade;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentRecordController {

    private final StudentRecordFacade studentRecordFacade;

    private static final String DEFAULT_SEMESTER = "2025-1";

    @PostMapping("/student-records/{recordType}/{recordId}")
    public ApiResponse<Void> updateStudentRecord(@PathVariable final StudentRecordType recordType,
                                                 @PathVariable final long recordId,
                                                 @RequestBody @Valid StudentRecordCreateRequest request) {
        studentRecordFacade.updateStudentRecord(1, recordType, recordId, request); // 멤버 아이디 하드코딩
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/student-records/{recordType}/{recordId}")
    public ApiResponse<StudentRecordDetailResponse> getStudentRecord(@PathVariable final StudentRecordType recordType,
                                                                     @PathVariable final long recordId,
                                                                     @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        return ApiResponse.success(CommonSuccessCode.OK, studentRecordFacade.getStudentRecord(1, recordType, recordId, semester)); // 멤버 아이디 하드코딩
    }

    @GetMapping("/student-records/{recordType}")
    public ApiResponse<StudentRecordOverviewsResponse> getStudentRecordOverviews(@PathVariable final StudentRecordType recordType,
                                                                                 @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        return ApiResponse.success(
                CommonSuccessCode.OK,
                studentRecordFacade.getStudentRecordOverviews(1, recordType, semester)
        ); // 멤버 아이디 하드코딩
    }
}
