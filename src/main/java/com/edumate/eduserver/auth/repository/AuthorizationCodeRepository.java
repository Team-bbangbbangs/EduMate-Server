package com.edumate.eduserver.auth.repository;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {
}
