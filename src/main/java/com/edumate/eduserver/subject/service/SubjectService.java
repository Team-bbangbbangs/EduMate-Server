package com.edumate.eduserver.subject.service;

import com.edumate.eduserver.subject.domain.Subject;
import com.edumate.eduserver.subject.exception.SubjectNotFoundException;
import com.edumate.eduserver.subject.exception.code.SubjectErrorCode;
import com.edumate.eduserver.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public Subject getSubjectByName(final String subjectName) {
        return subjectRepository.findByName(subjectName)
                .orElseThrow(() -> new SubjectNotFoundException(SubjectErrorCode.SUBJECT_NOT_FOUND));
    }
}
