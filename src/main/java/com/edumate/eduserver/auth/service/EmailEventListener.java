package com.edumate.eduserver.auth.service;

import com.edumate.eduserver.auth.facade.dto.MemberSignedUpEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailEvent(MemberSignedUpEvent event) {
        emailService.sendEmail(event.email(), event.memberUuid(), event.verificationCode());
    }
}
