package com.edumate.eduserver.studentrecord.facade;

import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import com.edumate.eduserver.studentrecord.service.StudentRecordService;
import com.edumate.eduserver.user.domain.Member;
import com.edumate.eduserver.user.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentRecordFacade {

    private final StudentRecordService studentRecordService;
    private final MemberService memberService;

    @Transactional
    public void updateStudentRecord(final String memberUuId, final long recordId, final String description,
                                    final int byteCount) {
        Member member = memberService.getMemberByUuid(memberUuId);
        studentRecordService.update(member.getId(), recordId, description, byteCount);
    }

    public StudentRecordDetailResponse getStudentRecord(final String memberUuid, final long recordId) {
        Member member = memberService.getMemberByUuid(memberUuid);
        StudentRecordDetail recordDetail = studentRecordService.getRecordDetail(member.getId(), recordId);
        return StudentRecordDetailResponse.of(recordDetail.getDescription(), recordDetail.getByteCount());
    }

    public StudentRecordOverviewsResponse getStudentRecordOverviews(final String memberUuId,
                                                                    final StudentRecordType recordType,
                                                                    final String semester) {
        Member member = memberService.getMemberByUuid(memberUuId);
        List<StudentRecordDetail> recordOverview = studentRecordService.getAll(member.getId(), recordType, semester);
        return StudentRecordOverviewsResponse.of(recordOverview);
    }

    public StudentNamesResponse getStudentDetails(final String memberUuid, final StudentRecordType recordType,
                                                  final String semester) {
        Member member = memberService.getMemberByUuid(memberUuid);
        List<StudentRecordDetail> studentRecordDetails = studentRecordService.getStudentNames(member.getId(), recordType, semester);
        return StudentNamesResponse.of(studentRecordDetails);
    }

    @Transactional
    public void createStudentRecords(final String memberUuid, final StudentRecordType recordType, final String semester,
                                     final List<StudentRecordInfo> studentRecordInfos) {
        Member member = memberService.getMemberByUuid(memberUuid);
        studentRecordService.createStudentRecords(member.getId(), recordType, semester, studentRecordInfos);
    }
}
