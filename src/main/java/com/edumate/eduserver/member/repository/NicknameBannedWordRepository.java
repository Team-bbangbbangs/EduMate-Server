package com.edumate.eduserver.member.repository;

import com.edumate.eduserver.member.domain.NicknameBannedWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NicknameBannedWordRepository extends JpaRepository<NicknameBannedWord, Long> {

    @Query("SELECT COUNT(w) > 0 FROM NicknameBannedWord w WHERE :nickname LIKE CONCAT('%', w.word, '%')")
    boolean existsBannedWordIn(@Param("nickname") String nickname);
}
