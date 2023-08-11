package com.boardgaming.domain.email.domain;

import com.boardgaming.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String recipient;
    private String subject;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String text;

    @Builder
    public EmailLog(
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
}
