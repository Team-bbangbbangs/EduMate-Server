package com.edumate.eduserver.external.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai.openai")
public record OpenAiProperties(
        String apiKey,
        String baseUrl,
        Integer timeoutSeconds
) {
} 
