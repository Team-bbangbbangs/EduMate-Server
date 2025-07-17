package com.edumate.eduserver.external.ai.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenAiRequest(
        String model,
        List<Message> messages,
        @JsonProperty("max_tokens") Integer maxTokens,
        Double temperature,
        @JsonProperty("response_format") ResponseFormat responseFormat
) {

    public record Message(
            String role,
            String content
    ) {}

    public record ResponseFormat(
            String type
    ) {}

    public static OpenAiRequest createChatRequest(String systemPrompt, String userPrompt) {
        return new OpenAiRequest(
                "gpt-4o",
                List.of(
                        new Message("system", systemPrompt),
                        new Message("user", userPrompt)
                ),
                4000,
                0.7,
                new ResponseFormat("json_object")
        );
    }
} 