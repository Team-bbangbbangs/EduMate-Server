package com.edumate.eduserver.studentrecord.repository;

import com.edumate.eduserver.studentrecord.domain.MemberStudentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStudentRecordRepository extends JpaRepository<MemberStudentRecord, Long> {
}
