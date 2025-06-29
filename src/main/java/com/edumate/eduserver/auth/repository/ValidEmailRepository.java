package com.edumate.eduserver.auth.repository;

import com.edumate.eduserver.auth.domain.ValidEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidEmailRepository extends JpaRepository<ValidEmail, Long> {
}
