package com.edumate.eduserver.external.ai.service;

import com.edumate.eduserver.external.ai.exception.OpenAiUnknownException;
import com.edumate.eduserver.external.ai.exception.code.OpenAiErrorCode;
import com.edumate.eduserver.external.ai.facade.response.StudentRecordAICreateResponse;
import com.edumate.eduserver.external.ai.service.dto.OpenAiRequest;
import com.edumate.eduserver.external.ai.service.dto.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_INSTRUCTIONS = """
            당신은 중고등학교 생활기록부 작성을 보조하는 AI 어시스턴트입니다.
            학생의 정보를 바탕으로 생활기록부를 작성합니다.
            """;

    public StudentRecordAICreateResponse getThreeChatResponses(final String prompt) {
        return getMultipleChatResponses(prompt);
    }

    private StudentRecordAICreateResponse getMultipleChatResponses(final String prompt) {
        try {
            OpenAiRequest request = OpenAiRequest.createChatRequest(SYSTEM_INSTRUCTIONS, prompt);
            OpenAiResponse response = openAiClient.chatCompletion(request);

            if (response.choices() == null || response.choices().isEmpty()) {
                log.error("[OpenAI] 응답에서 choices가 비어있음");
                throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
            }

            String content = response.choices().get(0).message().content();
            StudentRecordAICreateResponse parsedResponse = parseJsonResponse(content);
            
            log.info("[OpenAI] API 호출 및 파싱 성공");
            return parsedResponse;
            
        } catch (Exception e) {
            if (e instanceof OpenAiUnknownException || 
                e.getClass().getSimpleName().startsWith("OpenAi")) {
                throw e; // OpenAI 관련 예외는 그대로 재발생
            }
            log.error("[OpenAI] API 호출 중 예상치 못한 오류 발생: {}", e.getMessage());
            throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
        }
    }

    private StudentRecordAICreateResponse parseJsonResponse(String content) {
        try {
            return objectMapper.readValue(content, StudentRecordAICreateResponse.class);
        } catch (JsonProcessingException e) {
            log.error("[OpenAI] JSON 파싱 실패: {}", e.getMessage());
            log.debug("[OpenAI] 응답 내용: {}", content);
            throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
        }
    }
}
