package com.edumate.eduserver.studentrecord.repository;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStudentRecordRepository extends JpaRepository<MemberStudentRecord, Long> {

    Optional<MemberStudentRecord> findByMemberIdAndStudentRecordTypeAndSemester(long memberId, StudentRecordType studentRecordType, String semester);
}
