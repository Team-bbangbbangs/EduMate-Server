package com.edumate.eduserver.external.aws.ses;

import com.edumate.eduserver.auth.exception.IllegalUrlArgumentException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class AwsSesEmailMapper {

    private final TemplateEngine templateEngine;
    private final AwsSesProperties awsSesProperties;

    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
    private static final String EMAIL_SUBJECT = "EduMate 회원가입을 환영합니다.";

    public String buildEmailBody(final String memberUuid, final String verificationCode) {
        return buildVerificationEmail(memberUuid, verificationCode);
    }

    public SendEmailRequest buildSendEmailRequest(final String emailReceiver, final String htmlBody) {
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

    private String buildVerificationEmail(final String memberUuid, final String verificationCode) {
        Context context = new Context();
        context.setVariable("verificationLink", buildVerificationUrl(memberUuid, verificationCode));
        return templateEngine.process("email-verification", context);
    }

    private String buildVerificationUrl(final String memberUuid, final String verificationCode) {
        try {
            return String.format(awsSesProperties.emailVerifyUrl(), memberUuid, verificationCode);
        } catch (IllegalFormatException e) {
            log.error("Invalid format for email verification URL: {}", awsSesProperties.emailVerifyUrl(), e);
            throw new IllegalUrlArgumentException(AuthErrorCode.ILLEGAL_URL_ARGUMENT);
        }
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
}
