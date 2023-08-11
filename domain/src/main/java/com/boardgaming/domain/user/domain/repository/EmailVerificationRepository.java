package com.boardgaming.domain.user.domain.repository;

import com.boardgaming.domain.user.domain.EmailVerification;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmailAndSessionKey(
        final String email,
        final String sessionKey
    );
}
