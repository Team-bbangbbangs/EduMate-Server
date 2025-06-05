package com.edumate.eduserver.user.repository;

import com.edumate.eduserver.user.domain.NicknameBannedWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicknameBannedWordRepository extends JpaRepository<NicknameBannedWord, Long> {
}
