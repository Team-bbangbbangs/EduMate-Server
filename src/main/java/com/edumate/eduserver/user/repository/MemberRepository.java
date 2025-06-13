package com.edumate.eduserver.user.repository;

import com.edumate.eduserver.user.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberUuid(String memberUuid);

    boolean existsByEmail(String email);
}
