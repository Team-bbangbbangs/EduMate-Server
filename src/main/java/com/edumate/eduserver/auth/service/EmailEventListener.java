package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.facade.dto.MemberSignedUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailService emailService;

    @Async("emailTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailEvent(MemberSignedUpEvent event) {
        try {
            emailService.sendEmail(event.email(), event.memberUuid(), event.verificationCode());
        } catch (Exception e) {
            log.error("비동기 이메일 발송 실패. event={}", event, e);
        }
    }
}
