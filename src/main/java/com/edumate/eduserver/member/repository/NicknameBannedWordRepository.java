package com.edumate.eduserver.member.repository;

import com.edumate.eduserver.member.domain.NicknameBannedWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicknameBannedWordRepository extends JpaRepository<NicknameBannedWord, Long> {
}
