package com.edumate.eduserver.studentrecord.facade;

import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.member.service.MemberService;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordCreateInfo;
import com.edumate.eduserver.studentrecord.controller.request.vo.StudentRecordInfo;
import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import com.edumate.eduserver.studentrecord.facade.response.StudentNamesResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordDetailResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordOverviewsResponse;
import com.edumate.eduserver.studentrecord.facade.response.StudentRecordPromptResponse;
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
    private final MemberService memberService;

    @Transactional
    public void updateStudentRecord(final long memberId, final long recordId, final String description,
                                    final int byteCount) {
        studentRecordService.update(memberId, recordId, description, byteCount);
    }

    public StudentRecordDetailResponse getStudentRecord(final long memberId, final long recordId) {
        StudentRecordDetail recordDetail = studentRecordService.getRecordDetail(memberId, recordId);
        return StudentRecordDetailResponse.of(recordDetail.getDescription(), recordDetail.getByteCount());
    }

    public StudentRecordOverviewsResponse getStudentRecordOverviews(final long memberId,
                                                                    final StudentRecordType recordType,
                                                                    final String semester) {
        List<StudentRecordDetail> recordOverview = studentRecordService.getAll(memberId, recordType, semester);
        return StudentRecordOverviewsResponse.of(recordOverview);
    }

    public StudentNamesResponse getStudentDetails(final long memberId, final StudentRecordType recordType,
                                                  final String semester) {
        List<StudentRecordDetail> studentRecordDetails = studentRecordService.getStudentNames(memberId, recordType,
                semester);
        return StudentNamesResponse.of(studentRecordDetails);
    }

    @Transactional
    public void createStudentRecords(final long memberId, final StudentRecordType recordType, final String semester,
                                     final List<StudentRecordInfo> studentRecordInfos) {
        Member member = memberService.getMemberById(memberId);
        MemberStudentRecord studentRecord = studentRecordService.createSemesterRecord(member, recordType, semester);
        studentRecordService.createStudentRecords(studentRecord, studentRecordInfos);
    }

    @Transactional
    public void createStudentRecord(final long memberId, final StudentRecordType recordType, final String semester,
                                    final StudentRecordCreateInfo studentRecordCreateInfo) {
        studentRecordService.createStudentRecord(memberId, recordType, semester, studentRecordCreateInfo);
    }

    @Transactional
    public void updateStudentRecordOverview(final long memberId, final long recordId, final String studentNumber,
                                            final String studentName, final String description, final int byteCount) {
        studentRecordService.updateStudentRecordOverview(memberId, recordId, studentNumber, studentName,
                description, byteCount);
    }

    @Transactional
    public void deleteStudentRecord(final long memberId, final long recordId) {
        studentRecordService.deleteStudentRecord(memberId, recordId);
    }

    public StudentRecordPromptResponse getUserPrompt(final long memberId, final long recordId,
                                                     final String inputPrompt) {
        Member member = memberService.getMemberById(memberId);
        StudentRecordDetail recordDetail = studentRecordService.getRecordDetail(member.getId(), recordId);
        StudentRecordType recordType = recordDetail.getMemberStudentRecord().getStudentRecordType();
        return StudentRecordPromptResponse.of(recordType, inputPrompt);
    }
}
