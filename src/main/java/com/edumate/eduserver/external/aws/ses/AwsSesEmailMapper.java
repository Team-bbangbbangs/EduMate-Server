package com.edumate.eduserver.external.aws.ses;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class AwsSesEmailMapper {

    private final TemplateEngine templateEngine;

    public String buildVerificationEmail(final String verificationLink) {
        Context context = new Context();
        context.setVariable("verificationLink", verificationLink);
        return templateEngine.process("email-verification", context);
    }
}
