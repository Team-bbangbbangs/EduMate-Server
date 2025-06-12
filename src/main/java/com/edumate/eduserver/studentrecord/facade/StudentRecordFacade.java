package com.edumate.eduserver.studentrecord.facade;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentRecordFacade {

    private final StudentRecordService studentRecordService;

    @Transactional
    public void updateStudentRecord(final long memberId, final long recordId, final String description, final int byteCount) {
        studentRecordService.update(recordId, description, byteCount);
    }

    public StudentRecordDetailResponse getStudentRecord(final long memberId, final long recordId) {
        StudentRecordDetail recordDetail = studentRecordService.getRecordDetailById(recordId);
        return StudentRecordDetailResponse.of(recordDetail.getId(), recordDetail.getDescription(), recordDetail.getByteCount());
    }

    public StudentRecordOverviewsResponse getStudentRecordOverviews(final long memberId, final StudentRecordType recordType, final String semester) {
        List<StudentRecordDetail> recordOverview = studentRecordService.getAll(memberId, recordType, semester);
        return StudentRecordOverviewsResponse.of(recordOverview);
    }

    public StudentNamesResponse getStudentDetails(final long memberId, final StudentRecordType recordType, final String semester) {
        List<StudentRecordDetail> studentRecordDetails = studentRecordService.getStudentNames(memberId, recordType, semester);
        return StudentNamesResponse.of(studentRecordDetails);
    }

    @Transactional
    public void createStudentRecords(final long memberId, final StudentRecordType recordType, final String semester, final List<StudentRecordInfo> studentRecordInfos) {
        studentRecordService.createStudentRecords(memberId, recordType, semester, studentRecordInfos);
    }
}
