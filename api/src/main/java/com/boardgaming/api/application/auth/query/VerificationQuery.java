package com.boardgaming.api.application.auth.query;

import com.boardgaming.domain.user.domain.repository.VerifiedEmailRepository;
import com.boardgaming.common.exception.user.NotFoundEmailVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationQuery {
    private final VerifiedEmailRepository verifiedEmailRepository;

    public void checkEmailVerification(
        final String email,
        final String sessionKey
    ) {
        verifiedEmailRepository.findByEmailAndSessionKey(email, sessionKey)
            .orElseThrow(NotFoundEmailVerificationException::new);
    }
}
