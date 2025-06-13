package com.edumate.eduserver.studentrecord.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordUpdateRequest;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordsCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.StudentRecordFacade;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
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
@RequestMapping("/api/v1/student-records")
@RequiredArgsConstructor
public class StudentRecordController {

    private final StudentRecordFacade studentRecordFacade;

    private static final String DEFAULT_SEMESTER = "2025-1";

    @PostMapping("/detail/{recordId}")
    public ApiResponse<Void> updateStudentRecord(@PathVariable final long recordId,
                                                 @RequestBody @Valid final StudentRecordUpdateRequest request) {
        studentRecordFacade.updateStudentRecord(1, recordId, request.description().trim(), request.byteCount()); // 멤버 아이디 하드코딩
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/detail/{recordId}")
    public ApiResponse<StudentRecordDetailResponse> getStudentRecord(@PathVariable final long recordId) {
        return ApiResponse.success(CommonSuccessCode.OK, studentRecordFacade.getStudentRecord(1, recordId)); // 멤버 아이디 하드코딩
    }

    @GetMapping("/{recordType}")
    public ApiResponse<StudentRecordOverviewsResponse> getStudentRecordOverviews(@PathVariable final StudentRecordType recordType,
                                                                                 @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        return ApiResponse.success(CommonSuccessCode.OK, studentRecordFacade.getStudentRecordOverviews(1, recordType, semester.trim())); // 멤버 아이디 하드코딩
    }

    @GetMapping("/{recordType}/students")
    public ApiResponse<StudentNamesResponse> getStudentDetails(@PathVariable final StudentRecordType recordType,
                                                               @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        return ApiResponse.success(CommonSuccessCode.OK, studentRecordFacade.getStudentDetails(1, recordType, semester.trim())); // 멤버 아이디 하드코딩
    }

    @PostMapping("/{recordType}/students/batch")
    public ApiResponse<Void> createStudentRecords(@PathVariable final StudentRecordType recordType,
                                                  @RequestBody @Valid final StudentRecordsCreateRequest request) {
        studentRecordFacade.createStudentRecords(1, recordType, request.semester().trim(), request.studentRecords()); // 멤버 아이디 하드코딩
        return ApiResponse.success(CommonSuccessCode.CREATED);
    }
}
