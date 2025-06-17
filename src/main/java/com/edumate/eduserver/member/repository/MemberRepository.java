package com.edumate.eduserver.member.repository;

import com.edumate.eduserver.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberUuid(String memberUuid);

    Optional<Member> findByEmail(String email);

    boolean existsByEmailAndIsDeleted(String email, boolean isDeleted);

    Optional<Member> findByMemberUuidAndIsDeleted(String memberUuid, boolean isDeleted);
}
