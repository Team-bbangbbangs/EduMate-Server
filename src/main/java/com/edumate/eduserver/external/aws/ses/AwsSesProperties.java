package com.edumate.eduserver.external.aws.ses;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws.ses")
public record AwsSesProperties(
        String accessKey,
        String secretKey,
        String senderEmail,
        String region
) {
}
