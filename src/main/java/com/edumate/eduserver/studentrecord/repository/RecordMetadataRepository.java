package com.edumate.eduserver.studentrecord.repository;

import com.edumate.eduserver.studentrecord.domain.RecordMetadata;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordMetadataRepository extends JpaRepository<RecordMetadata, Long> {

    Optional<RecordMetadata> findByMemberIdAndStudentRecordTypeAndSemester(long memberId, StudentRecordType studentRecordType, String semester);
}
