package com.edumate.eduserver.auth.service;


import com.edumate.eduserver.external.aws.ses.AwsSesEmailMapper;
import com.edumate.eduserver.external.aws.ses.AwsSesProperties;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final SesClient sesClient;
    private final AwsSesEmailMapper awsSesEmailMapper;
    private final AwsSesProperties awsSesProperties;

    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    private static final String EMAIL_SUBJECT = "EduMate 회원가입을 환영합니다.";

    public void sendEmail(final String emailReceiver, final String memberUuid, final String verificationCode) {
        String htmlBody = buildEmailBody(memberUuid, verificationCode);
        SendEmailRequest request = buildSendEmailRequest(emailReceiver, htmlBody);
        SendEmailResponse result = sesClient.sendEmail(request);
        validateSendResult(emailReceiver, result);
    }

    private String buildEmailBody(final String memberUuid, final String verificationCode) {
        return awsSesEmailMapper.buildVerificationEmail(memberUuid, verificationCode);
    }

    private SendEmailRequest buildSendEmailRequest(final String emailReceiver, final String htmlBody) {
        Destination destination = createDestination(emailReceiver);
        Content subjectContent = createSubjectContent();
        Content htmlBodyContent = createHtmlBodyContent(htmlBody);
        Body body = createBody(htmlBodyContent);
        Message message = createMessage(subjectContent, body);

        return SendEmailRequest.builder()
                .source(awsSesProperties.senderEmail())
                .destination(destination)
                .message(message)
                .build();
    }

    private Destination createDestination(final String emailReceiver) {
        return Destination.builder()
                .toAddresses(emailReceiver)
                .build();
    }

    private Content createSubjectContent() {
        return Content.builder()
                .data(EMAIL_SUBJECT)
                .charset(DEFAULT_CHARSET)
                .build();
    }

    private Content createHtmlBodyContent(final String htmlBody) {
        return Content.builder()
                .data(htmlBody)
                .charset(DEFAULT_CHARSET)
                .build();
    }

    private Body createBody(final Content htmlBodyContent) {
        return Body.builder()
                .html(htmlBodyContent)
                .build();
    }

    private Message createMessage(final Content subjectContent, final Body body) {
        return Message.builder()
                .subject(subjectContent)
                .body(body)
                .build();
    }

    private void validateSendResult(final String emailReceiver, final SdkResponse result) {
        SdkHttpResponse httpResponse = result.sdkHttpResponse();
        if (httpResponse.isSuccessful()) {
            log.info("Email sent successfully to {}. Response: {}", emailReceiver, result);
        } else {
            log.error("Failed to send email to {}. Response: {}. Status Code: {}", emailReceiver, result, httpResponse.statusCode());
        }
    }
}
