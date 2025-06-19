package com.edumate.eduserver.external.aws.ses;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final SesClient sesClient;
    private final AwsSesEmailMapper awsSesEmailMapper;

    public void sendEmail(final String emailReceiver, final String memberUuid, final String verificationCode) {
        String htmlBody = awsSesEmailMapper.buildEmailBody(memberUuid, verificationCode);
        SendEmailRequest request = awsSesEmailMapper.buildSendEmailRequest(emailReceiver, htmlBody);
        SendEmailResponse result = sesClient.sendEmail(request);
        validateSendResult(emailReceiver, result);
    }

    private void validateSendResult(final String emailReceiver, final SdkResponse result) {
        SdkHttpResponse httpResponse = result.sdkHttpResponse();
        if (httpResponse.isSuccessful()) {
            log.info("Email sent successfully to {}. Response: {}", emailReceiver, result);
        } else {
            log.error("Failed to send email to {}. Response: {}. Status Code: {}", emailReceiver, result,
                    httpResponse.statusCode());
        }
    }
}
