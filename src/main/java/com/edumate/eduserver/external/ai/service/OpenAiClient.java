package com.edumate.eduserver.external.ai.service;

import com.edumate.eduserver.external.ai.config.OpenAiProperties;
import com.edumate.eduserver.external.ai.exception.OpenAiConnectionTimeoutException;
import com.edumate.eduserver.external.ai.exception.OpenAiQuotaExceededException;
import com.edumate.eduserver.external.ai.exception.OpenAiRateLimitExceededException;
import com.edumate.eduserver.external.ai.exception.OpenAiReadTimeoutException;
import com.edumate.eduserver.external.ai.exception.OpenAiUnknownException;
import com.edumate.eduserver.external.ai.exception.code.OpenAiErrorCode;
import com.edumate.eduserver.external.ai.service.dto.OpenAiRequest;
import com.edumate.eduserver.external.ai.service.dto.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestTemplate restTemplate;
    private final OpenAiProperties openAiProperties;
    private final ObjectMapper objectMapper;

    private static final String CHAT_COMPLETIONS_ENDPOINT = "/chat/completions";

    public OpenAiResponse chatCompletion(OpenAiRequest request) {
        try {
            String url = openAiProperties.baseUrl() + CHAT_COMPLETIONS_ENDPOINT;
            HttpHeaders headers = createHeaders();
            HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);

            log.info("[OpenAI] API 호출 시작: model={}", request.model());
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.info("[OpenAI] API 호출 성공");
            return parseOpenAiResponse(response.getBody());

        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException || 
                e.getCause() instanceof TimeoutException ||
                e.getMessage().contains("timeout")) {
                log.error("[OpenAI] Connection timeout: {}", e.getMessage());
                throw new OpenAiConnectionTimeoutException(OpenAiErrorCode.CONNECTION_TIMEOUT);
            } else {
                log.error("[OpenAI] Read timeout: {}", e.getMessage());
                throw new OpenAiReadTimeoutException(OpenAiErrorCode.READ_TIMEOUT);
            }
        } catch (HttpStatusCodeException e) {
            handleHttpStatusError(e);
            throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
        } catch (Exception e) {
            log.error("[OpenAI] API 호출 중 오류 발생: {}", e.getMessage());
            throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAiProperties.apiKey());
        return headers;
    }

    private void handleHttpStatusError(HttpStatusCodeException e) {
        HttpStatus status = HttpStatus.resolve(e.getStatusCode().value());
        String body = e.getResponseBodyAsString();
        
        if (status == HttpStatus.TOO_MANY_REQUESTS) {
            if (body.contains("rate_limit_exceeded")) {
                log.error("[OpenAI] Rate Limit 초과: {}", e.getMessage());
                throw new OpenAiRateLimitExceededException(OpenAiErrorCode.RATE_LIMIT_EXCEEDED);
            } else if (body.contains("insufficient_quota")) {
                log.error("[OpenAI] Credit 소진: {}", e.getMessage());
                throw new OpenAiQuotaExceededException(OpenAiErrorCode.QUOTA_EXCEEDED);
            }
        }
        
        log.error("[OpenAI] API 응답 에러 처리되지 않음: status={}, body={}", status, body);
    }

    private OpenAiResponse parseOpenAiResponse(String responseBody) {
        try {
            if (responseBody == null || responseBody.trim().isEmpty()) {
                log.error("[OpenAI] 응답 내용이 비어있음");
                throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
            }
            
            log.info("[OpenAI] JSON 파싱 시작");
            OpenAiResponse response = objectMapper.readValue(responseBody, OpenAiResponse.class);
            log.info("[OpenAI] JSON 파싱 성공");
            
            return response;
        } catch (JsonProcessingException e) {
            log.error("[OpenAI] JSON 파싱 실패: {}", e.getMessage());
            log.error("[OpenAI] 파싱 오류 상세: {}", e.getOriginalMessage());
            log.error("[OpenAI] 응답 내용: {}", responseBody);
            throw new OpenAiUnknownException(OpenAiErrorCode.UNKNOWN_ERROR);
        }
    }
} 
