package com.edumate.eduserver.auth.repository;

import com.edumate.eduserver.auth.domain.AuthorizationCode;
import com.edumate.eduserver.auth.domain.AuthorizeStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {

    Optional<AuthorizationCode> findTop1ByMemberIdAndStatusOrderByIdDesc(long memberId, AuthorizeStatus status);
}
