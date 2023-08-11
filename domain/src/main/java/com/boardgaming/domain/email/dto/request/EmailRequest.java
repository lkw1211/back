package com.boardgaming.domain.email.dto.request;

import com.boardgaming.domain.email.domain.EmailLog;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailRequest {
    private String sender;
    private String recipient;
    private String subject;
    private String text;

    @Builder
    public EmailRequest(
        final String sender,
        final String recipient,
        final String subject,
        final String text
    ) {
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.text = text;
    }

    public EmailLog toEntity() {
        return EmailLog.builder()
            .sender(sender)
            .recipient(recipient)
            .subject(subject)
            .text(text)
            .build();
    }
}
