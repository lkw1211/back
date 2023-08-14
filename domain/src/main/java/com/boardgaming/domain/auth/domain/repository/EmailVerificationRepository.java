package com.boardgaming.domain.auth.domain.repository;

import com.boardgaming.domain.auth.domain.EmailVerification;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmailAndSessionKey(
        final String email,
        final String sessionKey
    );
}
