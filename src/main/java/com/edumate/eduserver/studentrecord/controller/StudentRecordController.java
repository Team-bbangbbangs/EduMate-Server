package com.edumate.eduserver.studentrecord.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.annotation.MemberId;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordOverviewUpdateRequest;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordPromptRequest;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordUpdateRequest;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordsCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.StudentRecordFacade;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordAICreateResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ApiResponse<Void> updateStudentRecord(@MemberId final long memberId, @PathVariable final long recordId,
                                                 @RequestBody @Valid final StudentRecordUpdateRequest request) {
        studentRecordFacade.updateStudentRecord(memberId, recordId, request.description().strip(), request.byteCount());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @GetMapping("/detail/{recordId}")
    public ApiResponse<StudentRecordDetailResponse> getStudentRecord(@MemberId final long memberId,
                                                                     @PathVariable final long recordId) {
        StudentRecordDetailResponse response = studentRecordFacade.getStudentRecord(memberId, recordId);
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @GetMapping("/{recordType}")
    public ApiResponse<StudentRecordOverviewsResponse> getStudentRecordOverviews(@MemberId final long memberId,
                                                                                 @PathVariable final StudentRecordType recordType,
                                                                                 @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        StudentRecordOverviewsResponse response = studentRecordFacade.getStudentRecordOverviews(memberId, recordType,
                semester.strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @GetMapping("/{recordType}/students")
    public ApiResponse<StudentNamesResponse> getStudentDetails(@MemberId final long memberId,
                                                               @PathVariable final StudentRecordType recordType,
                                                               @RequestParam(defaultValue = DEFAULT_SEMESTER) final String semester) {
        StudentNamesResponse response = studentRecordFacade.getStudentDetails(memberId, recordType, semester.strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }

    @PostMapping("/{recordType}/students/batch")
    public ApiResponse<Void> createStudentRecords(@MemberId final long memberId,
                                                  @PathVariable final StudentRecordType recordType,
                                                  @RequestBody @Valid final StudentRecordsCreateRequest request) {
        studentRecordFacade.createStudentRecords(memberId, recordType, request.semester().strip(),
                request.studentRecords());
        return ApiResponse.success(CommonSuccessCode.CREATED);
    }

    @PostMapping("/{recordType}/students")
    public ApiResponse<Void> createStudentRecord(@MemberId final long memberId,
                                                 @PathVariable final StudentRecordType recordType,
                                                 @RequestBody @Valid final StudentRecordCreateRequest request) {
        studentRecordFacade.createStudentRecord(memberId, recordType, request.semester().strip(),
                request.studentRecord());
        return ApiResponse.success(CommonSuccessCode.CREATED);
    }

    @PatchMapping("/{recordId}")
    public ApiResponse<Void> updateStudentRecordOverview(@MemberId final long memberId,
                                                         @PathVariable final long recordId,
                                                         @RequestBody @Valid final StudentRecordOverviewUpdateRequest request) {
        studentRecordFacade.updateStudentRecordOverview(memberId, recordId, request.studentNumber().strip(),
                request.studentName().strip(), request.description().strip(), request.byteCount());
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @DeleteMapping("/{recordId}")
    public ApiResponse<Void> deleteStudentRecord(@MemberId final long memberId,
                                                 @PathVariable final long recordId) {
        studentRecordFacade.deleteStudentRecord(memberId, recordId);
        return ApiResponse.success(CommonSuccessCode.OK);
    }

    @PostMapping("/ai-generate/{recordId}")
    public ApiResponse<StudentRecordAICreateResponse> aiGenerateStudentRecord(@MemberId final long memberId,
                                                                              @PathVariable final long recordId,
                                                                              @RequestBody @Valid final StudentRecordPromptRequest request) {
        StudentRecordAICreateResponse response = studentRecordFacade.generateAIStudentRecord(memberId, recordId, request.prompt().strip());
        return ApiResponse.success(CommonSuccessCode.OK, response);
    }
}
