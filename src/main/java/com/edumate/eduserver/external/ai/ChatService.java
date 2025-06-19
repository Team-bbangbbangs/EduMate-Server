package com.edumate.eduserver.external.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(OpenAIProperties.class)
public class ChatService {

    private final OpenAIProperties openAIProperties;
    private final ChatClient chatClient;

    public String getChatResponse(final String prompt) {
        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("OpenAI API 호출 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
