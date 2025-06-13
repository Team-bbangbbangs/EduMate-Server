package com.edumate.eduserver.subject.repository;

import com.edumate.eduserver.subject.domain.Subject;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);
}
