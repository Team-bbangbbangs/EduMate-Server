package com.edumate.eduserver.auth.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.edumate.eduserver.auth.exception.EmailSendFailedException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import com.edumate.eduserver.external.aws.ses.AwsSesEmailMapper;
import com.edumate.eduserver.external.aws.ses.AwsSesProperties;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final AwsSesEmailMapper awsSesEmailMapper;
    private final AwsSesProperties awsSesProperties;

    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    private static final String EMAIL_SUBJECT = "EduMate 회원가입을 환영합니다.";

    public void sendEmail(final String to, final String memberUuid, final String verificationCode) {
        String htmlBody = buildEmailBody(memberUuid, verificationCode);
        SendEmailRequest request = buildSendEmailRequest(to, htmlBody);
        SendEmailResult result = amazonSimpleEmailService.sendEmail(request);
        validateSendResult(to, result);
    }

    private String buildEmailBody(final String memberUuid, final String verificationCode) {
        return awsSesEmailMapper.buildVerificationEmail(memberUuid, verificationCode);
    }

    private SendEmailRequest buildSendEmailRequest(final String to, final String htmlBody) {
        Destination destination = new Destination().withToAddresses(to);
        Message message = new Message()
                .withSubject(new Content(EMAIL_SUBJECT).withCharset(DEFAULT_CHARSET))
                .withBody(new Body().withHtml(new Content().withCharset(DEFAULT_CHARSET).withData(htmlBody)));

        return new SendEmailRequest()
                .withSource(awsSesProperties.senderEmail())
                .withDestination(destination)
                .withMessage(message);
    }

    private void validateSendResult(final String to, final SendEmailResult result) {
        int statusCode = result.getSdkHttpMetadata().getHttpStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            log.error("Failed to send email to {}. Response: {}", to, result);
            throw new EmailSendFailedException(AuthErrorCode.EMAIL_SEND_FAILED);
        }
    }
}
