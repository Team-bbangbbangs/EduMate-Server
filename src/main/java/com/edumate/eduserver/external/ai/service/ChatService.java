package com.edumate.eduserver.external.ai.service;

import com.edumate.eduserver.external.ai.exception.OpenAiQuotaExceededException;
import com.edumate.eduserver.external.ai.exception.OpenAiRateLimitExceededException;
import com.edumate.eduserver.external.ai.exception.OpenAiUnknownException;
import com.edumate.eduserver.external.ai.exception.code.OpenAiErrorCode;
import com.edumate.eduserver.external.ai.facade.response.StudentRecordAICreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

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
                    .system("""
                            당신은 중고등학교 생활기록부 작성을 보조하는 AI 어시스턴트입니다.
                            학생의 정보를 바탕으로 생활기록부를 작성합니다.
                            
                            중요: 응답은 반드시 유효한 JSON 형식이어야 하며, 
                            JSON 외의 다른 텍스트나 마크다운은 포함하지 마세요.
                            """)
                    .user(prompt)
                    .call()
                    .entity(StudentRecordAICreateResponse.class);
        } catch (HttpStatusCodeException e) {
            HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
            String body = e.getResponseBodyAsString();
            if (status == HttpStatus.TOO_MANY_REQUESTS) {
                if (body.contains("rate_limit_exceeded")) {
                    log.error("OpenAI Rate Limit 초과: {}", e.getMessage());
                    throw new OpenAiRateLimitExceededException(OpenAiErrorCode.RATE_LIMIT_EXCEEDED);
                } else if (body.contains("insufficient_quota")) {
                    log.error("OpenAI Credit 소진: {}", e.getMessage());
                    throw new OpenAiQuotaExceededException(OpenAiErrorCode.QUOTA_EXCEEDED);
                }
            }
            log.error("OpenAI API 응답 에러 처리되지 않음: {}", body);
            throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
        } catch (Exception e) {
            log.error("OpenAI API 호출 중 오류 발생: {}", e.getMessage());
            throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
        }
    }
}
