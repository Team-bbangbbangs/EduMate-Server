package com.edumate.eduserver.external.aws.ses.config;

import com.edumate.eduserver.external.aws.ses.AwsSesProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsSesProperties.class)
public class AwsSesConfig {

    private final AwsSesProperties awsSesProperties;

    @Bean
    SesClient createSesClient() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsSesProperties.accessKey(), awsSesProperties.secretKey());
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCredentials);
        return SesClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(awsSesProperties.region()))
                .build();
    }
}
