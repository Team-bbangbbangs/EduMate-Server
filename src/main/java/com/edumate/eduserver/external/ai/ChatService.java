package com.edumate.eduserver.external.ai;

import com.edumate.eduserver.studentrecord.facade.response.StudentRecordAICreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    public StudentRecordAICreateResponse getThreeChatResponses(final String prompt) {
        return getMultipleChatResponses(prompt);
    }

    private StudentRecordAICreateResponse getMultipleChatResponses(final String prompt) {
        try {
            return chatClient.prompt()
                    .system("당신은 중고등학교 생활기록부 작성을 보조하는 AI 어시스턴트입니다. 학생의 정보를 바탕으로 생활기록부를 작성합니다.")
                    .user(prompt)
                    .call()
                    .entity(StudentRecordAICreateResponse.class);


        } catch (Exception ex) {
            log.error("OpenAI API 호출 중 오류 발생: {}", ex.getMessage(), ex);
            throw new RuntimeException("OpenAI API 호출 중 오류가 발생했습니다: " + ex.getMessage());
        }
    }
}
