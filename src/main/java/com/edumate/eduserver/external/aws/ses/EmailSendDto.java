package com.edumate.eduserver.external.aws.ses;

public record EmailSendDto(
        String receiver,
        String subject,
        String content
) {
}
