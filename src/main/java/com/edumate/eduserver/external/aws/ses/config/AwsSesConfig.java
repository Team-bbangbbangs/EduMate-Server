package com.edumate.eduserver.external.aws.ses.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.edumate.eduserver.external.aws.ses.AwsSesProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsSesProperties.class)
public class AwsSesConfig {

    private final AwsSesProperties awsSesProperties;

    @Bean
    AmazonSimpleEmailService createAmazonSimpleEmailService() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsSesProperties.accessKey(), awsSesProperties.secretKey());
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        return AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(awsSesProperties.region())
                .build();
    }
}
