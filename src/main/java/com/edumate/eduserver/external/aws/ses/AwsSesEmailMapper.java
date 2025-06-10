package com.edumate.eduserver.external.aws.ses;

import com.edumate.eduserver.auth.exception.IllegalUrlArgumentException;
import com.edumate.eduserver.auth.exception.code.AuthErrorCode;
import java.util.IllegalFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@Slf4j
@RequiredArgsConstructor
public class AwsSesEmailMapper {

    private final TemplateEngine templateEngine;
    private final AwsSesProperties awsSesProperties;

    public String buildVerificationEmail(final String memberUuid, final String verificationCode) {
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
}
