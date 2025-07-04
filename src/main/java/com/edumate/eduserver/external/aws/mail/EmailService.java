package com.edumate.eduserver.external.aws.mail;


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
        SendEmailRequest request = awsSesEmailMapper.buildEmailRequestForSignUp(emailReceiver, memberUuid, verificationCode);
        send(request, emailReceiver, memberUuid);
    }

    public void sendEmailForEmailUpdate(final String emailReceiver, final String memberUuid, final String verificationCode) {
        SendEmailRequest request = awsSesEmailMapper.buildEmailRequestForEmailUpdate(emailReceiver, memberUuid, verificationCode);
        send(request, emailReceiver, memberUuid);
    }

    private void send(final SendEmailRequest request, final String emailReceiver, final String memberUuid) {
        log.info("[SES] 이메일 발송 요청: to={}, memberUuid={}", emailReceiver, memberUuid);
        SendEmailResponse result = sesClient.sendEmail(request);
        log.info("[SES] 이메일 발송 응답: to={}, messageId={}, status={} ", emailReceiver, result.messageId(), result.sdkHttpResponse().statusCode());
        validateSendResult(emailReceiver, result);
    }

    private void validateSendResult(final String emailReceiver, final SdkResponse result) {
        SdkHttpResponse httpResponse = result.sdkHttpResponse();
        if (httpResponse.isSuccessful()) {
            log.info("[SES] 이메일 발송 성공: to={}", emailReceiver);
        } else {
            log.error("[SES] 이메일 발송 실패: to={}, reason={}", emailReceiver, httpResponse.statusText().orElse("unknown"));
        }
    }
}
