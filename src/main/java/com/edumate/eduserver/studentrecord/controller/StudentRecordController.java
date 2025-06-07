package com.edumate.eduserver.studentrecord.controller;

import com.edumate.eduserver.common.ApiResponse;
import com.edumate.eduserver.common.code.CommonSuccessCode;
import com.edumate.eduserver.studentrecord.controller.request.StudentRecordCreateRequest;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.StudentRecordFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StudentRecordController {

    private final StudentRecordFacade studentRecordFacade;

    @PostMapping("/student-records/{recordType}/{studentId}")
    public ApiResponse<Void> createStudentRecord(@PathVariable final StudentRecordType recordType,
                                                 @PathVariable final long studentId,
                                                 @RequestBody @Valid StudentRecordCreateRequest request) {
        studentRecordFacade.createStudentRecord(1, recordType, studentId, request); // 멤버 아이디 하드코딩
        return ApiResponse.success(CommonSuccessCode.OK);
    }
}
