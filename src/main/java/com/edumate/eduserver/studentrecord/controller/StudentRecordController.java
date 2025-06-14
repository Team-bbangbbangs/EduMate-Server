package com.edumate.eduserver.studentrecord.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.annotation.MemberUuid;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
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
    public ApiResponse<Void> updateStudentRecord(@MemberUuid final String memberUuid, @PathVariable final long recordId,
                                                 @RequestBody @Valid final StudentRecordUpdateRequest request) {
        studentRecordFacade.updateStudentRecord(memberUuid.strip(), recordId, request.description().strip(), request.byteCount());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/detail/{recordId}")
    public ApiResponse<StudentRecordDetailResponse> getStudentRecord(@MemberUuid final String memberUuid,
                                                                     @PathVariable final long recordId) {
        StudentRecordDetailResponse response = studentRecordFacade.getStudentRecord(memberUuid.strip(), recordId);
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @GetMapping("/{recordType}")
    public ApiResponse<StudentRecordOverviewsResponse> getStudentRecordOverviews(@MemberUuid final String memberUuid,
                                                                                 @PathVariable final StudentRecordType recordType,
                                                                                 @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        StudentRecordOverviewsResponse response = studentRecordFacade.getStudentRecordOverviews(memberUuid.strip(), recordType, semester.strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @GetMapping("/{recordType}/students")
    public ApiResponse<StudentNamesResponse> getStudentDetails(@MemberUuid final String memberUuid,
                                                               @PathVariable final StudentRecordType recordType,
                                                               @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        StudentNamesResponse response = studentRecordFacade.getStudentDetails(memberUuid.strip(), recordType, semester.strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @PostMapping("/{recordType}/students/batch")
    public ApiResponse<Void> createStudentRecords(@MemberUuid final String memberUuid,
                                                  @PathVariable final StudentRecordType recordType,
                                                  @RequestBody @Valid final StudentRecordsCreateRequest request) {
        studentRecordFacade.createStudentRecords(memberUuid.strip(), recordType, request.semester().strip(), request.studentRecords());
        return ApiResponse.success(CommonSuccessCode.CREATED);
    }

    @PostMapping("/{recordType}/students")
    public ApiResponse<Void> createStudentRecord(@MemberUuid final String memberUuid,
                                                 @PathVariable final StudentRecordType recordType,
                                                 @RequestBody @Valid final StudentRecordCreateRequest request) {
        studentRecordFacade.createStudentRecord(memberUuid.strip(), recordType, request.semester().trim(), request.studentRecord()); // 멤버 아이디 하드코딩
        return ApiResponse.success(CommonSuccessCode.CREATED);
    }
}
