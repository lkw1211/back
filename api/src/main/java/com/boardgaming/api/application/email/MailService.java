package com.boardgaming.api.application.email;

import com.boardgaming.domain.email.domain.EmailLog;
import com.boardgaming.domain.email.domain.repository.EmailLogRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class MailService {
    private final String sender;
    private final String chars;
    private final Integer codeLen;
    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;
    private final String logoUrl;

    public MailService(
            @Value("${spring.mail.verification.sender}") final String sender,
            @Value("${spring.mail.verification.chars}") final String chars,
            @Value("${spring.mail.verification.codeLen}") final Integer codeLen,
            final JavaMailSender mailSender,
            final EmailLogRepository emailLogRepository,
            @Value("${spring.mail.verification.logoUrl}") final String logoUrl
    ) {
        this.sender = sender;
        this.chars = chars;
        this.codeLen = codeLen;
        this.mailSender = mailSender;
        this.emailLogRepository = emailLogRepository;
        this.logoUrl = logoUrl;
    }

    @Transactional
    public String sendVerificationCode(final String recipient) {
        String verificationCode = generateVerificationCode();

        sendEmail(recipient, "boardgame verification code", getVerificationHtml(verificationCode));

        return verificationCode;
    }

    private String getVerificationHtml(final String verificationCode) {
        return "<table width='100%' cellspacing='0' cellpadding='0' border='0' align='center' bgcolor='#ffffff' style='max-width:640px;margin:0 auto;padding:10px'>" +
                "<tbody>" +
                "<tr>" +
                "<td height='60px'></td>" +
                "</tr>" +
                "<tr>" +
                "<td style='text-align:center'>" +
                "<h1>BoardGame</h1>" +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td height='8px'></td>" +
                "</tr>" +
                "<tr>" +
                "<td style='font-size:16px;line-height:24px;letter-spacing:-0.32px'>" +
                "Please enter the verification code below on the email authentication screen and complete the verification." +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td height='32px'></td>" +
                "</tr>" +
                "<tr>" +
                "<td style='font-size:2em;font-weight:bold;line-height:41px;letter-spacing:-1.02px;text-align:center'>" +
                verificationCode +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td height='48px'></td>" +
                "</tr>" +
                "<tr>" +
                "<td height='1px' bgcolor='#DDE2E6'></td>" +
                "</tr>" +
                "<tr>" +
                "<td height='48px'></td>" +
                "</tr>" +
                "<tr>" +
                "<td style='font-size:16px;line-height:24px;letter-spacing:-0.32px;word-break:keep-all'>" +
                "If you did not initiate the authentication email yourself, please disregard it." +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td height='48px'></td>" +
                "</tr>" +
                "<tr>" +
                "<td height='2px' bgcolor='#212529'></td>" +
                "</tr>" +
                "<tr>" +
                "<td height='32px'></td>" +
                "</tr>" +
                "<tr>" +
                "<td style='font-size:13px;line-height:20px;letter-spacing:-0.26px;color:#868e96'>" +
                "This email has been sent from <a href='" + logoUrl + "' style='text-decoration:none'>BoardGame</a>." +
                "</td>" +
                "</tr>" +
                "<tr>" +
                "<td height='80px'></td>" +
                "</tr>" +
                "</tbody>" +
                "</table>";
    }

    private void sendEmail(
        final String recipient,
        final String subject,
        final String text
    ) {
        emailLogRepository.save(EmailLog.builder()
                .recipient(recipient)
                .sender(sender)
                .subject(subject)
                .text(text)
            .build());
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setTo(recipient);
            helper.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder(codeLen);

        for (int i = 0; i < codeLen; i++) {
            int randomIndex = random.nextInt(chars.length());
            char randomChar = chars.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }

        return codeBuilder.toString();
    }
}

