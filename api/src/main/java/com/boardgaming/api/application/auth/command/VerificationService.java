package com.boardgaming.api.application.auth.command;

import com.boardgaming.api.application.email.MailService;
import com.boardgaming.domain.user.domain.EmailVerification;
import com.boardgaming.domain.user.domain.VerifiedEmail;
import com.boardgaming.domain.user.domain.repository.EmailVerificationRepository;
import com.boardgaming.domain.user.domain.repository.VerifiedEmailRepository;
import com.boardgaming.domain.user.dto.request.CheckEmailVerificationCodeRequest;
import com.boardgaming.domain.user.dto.request.SendEmailVerificationCodeRequest;
import com.boardgaming.domain.user.dto.response.ValidCheckResponse;
import com.boardgaming.common.exception.user.NotSentEmailVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final VerifiedEmailRepository verifiedEmailRepository;
    private final MailService mailService;

    public ValidCheckResponse checkEmailVerificationCode(final CheckEmailVerificationCodeRequest request) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmailAndSessionKey(
            request.getEmail(),
            request.getSessionKey()
        ).orElseThrow(NotSentEmailVerificationException::new);

        if (emailVerification.getVerificationCode().equals(request.getVerificationCode())) {
            verifiedEmailRepository.save(
                VerifiedEmail.builder()
                    .email(request.getEmail())
                    .sessionKey(request.getSessionKey())
                    .build());

            return new ValidCheckResponse(true);
        }

        return ValidCheckResponse.of(false);
    }

    public void sendEmailVerificationCode(final SendEmailVerificationCodeRequest request) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmailAndSessionKey(request.getEmail(), request.getSessionKey())
            .orElseGet(() -> EmailVerification.builder()
                .sessionKey(request.getSessionKey())
                .email(request.getEmail())
                .build());

        String verificationCode = mailService.sendVerificationCode(request.getEmail());
        log.info("------------------------------------");
        log.info("verification code: " + verificationCode);
        log.info("------------------------------------");

        emailVerification.updateVerification(verificationCode);

        emailVerificationRepository.save(emailVerification);
    }
}
