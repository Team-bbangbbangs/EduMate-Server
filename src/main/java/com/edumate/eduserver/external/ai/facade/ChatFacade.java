package com.edumate.eduserver.external.ai.facade;

import com.edumate.eduserver.external.ai.facade.response.StudentRecordAICreateResponse;
import com.edumate.eduserver.external.ai.service.ChatService;
import com.edumate.eduserver.external.ai.service.PromptService;
import com.edumate.eduserver.member.domain.Member;
import com.edumate.eduserver.studentrecord.domain.StudentRecordType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatFacade {

    private final ChatService chatService;
    private final PromptService promptService;

    public StudentRecordAICreateResponse generateAIStudentRecord(final Member member,
                                                                 final StudentRecordType recordType,
                                                                 final String inputPrompt) {
        String prompt = promptService.createPrompt(member.getSubject().getName(), recordType, inputPrompt);
        return chatService.getThreeChatResponses(prompt);
    }
}
