package com.edumate.eduserver.external.aws.ses;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class AwsSesEmailMapper {

    private static final String SENDER = "libra001008@gmail.com";

    public SendEmailRequest toSendEmailRequest(final EmailSendDto emailSendDto) {
        Destination destination = new Destination().withToAddresses(emailSendDto.receiver());
        Message message = new Message().withSubject(createContent(emailSendDto.subject()))
                .withBody(new Body().withHtml(createContent(emailSendDto.content())));
        return new SendEmailRequest().withSource(SENDER)
                .withDestination(destination)
                .withMessage(message);
    }

    private Content createContent(final String text) {
        return new Content().withCharset(StandardCharsets.UTF_8.name()).withData(text);
    }
}
