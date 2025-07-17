package com.edumate.eduserver.external.ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(OpenAiProperties.class)
public class ChatConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, OpenAiProperties openAiProperties) {
        return builder
                .connectTimeout(Duration.ofSeconds(openAiProperties.connectTimeoutSeconds()))
                .readTimeout(Duration.ofSeconds(openAiProperties.readTimeoutSeconds()))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.build();
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        return mapper;
    }
}
