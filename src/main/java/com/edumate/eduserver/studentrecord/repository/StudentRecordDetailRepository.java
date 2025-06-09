package com.edumate.eduserver.studentrecord.repository;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRecordDetailRepository extends JpaRepository<StudentRecordDetail, Long> {

    Optional<StudentRecordDetail> findByIdAndMemberStudentRecord(long recordId, MemberStudentRecord memberStudentRecord);

    List<StudentRecordDetail> findAllByMemberStudentRecordOrderByCreatedAtAsc(MemberStudentRecord memberStudentRecord);
}
