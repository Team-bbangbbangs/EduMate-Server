package com.edumate.eduserver.studentrecord.repository;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRecordDetailRepository extends JpaRepository<StudentRecordDetail, Long> {

    List<StudentRecordDetail> findAllByMemberStudentRecordOrderByCreatedAtAsc(MemberStudentRecord memberStudentRecord);
}
