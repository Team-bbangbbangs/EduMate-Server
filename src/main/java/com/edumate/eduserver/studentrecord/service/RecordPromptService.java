package com.edumate.eduserver.studentrecord.service;

import com.edumate.eduserver.studentrecord.domain.RecordPrompt;
import com.edumate.eduserver.studentrecord.domain.StudentRecordDetail;
import com.edumate.eduserver.studentrecord.repository.RecordPromptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordPromptService {

    private final RecordPromptRepository recordPromptRepository;

    @Transactional
    public void saveRecordPrompt(final StudentRecordDetail recordDetail, final String prompt) {
        RecordPrompt recordPrompt = RecordPrompt.create(recordDetail, prompt);
        recordPromptRepository.save(recordPrompt);
    }
}
