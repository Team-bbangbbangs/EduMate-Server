package com.edumate.eduserver.external.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.ai.openai")
public record OpenAIProperties(
        String apiKey,
        String model
) {
}
