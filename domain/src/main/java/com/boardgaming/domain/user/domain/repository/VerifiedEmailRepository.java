package com.boardgaming.domain.user.domain.repository;

import com.boardgaming.domain.user.domain.VerifiedEmail;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerifiedEmailRepository extends CrudRepository<VerifiedEmail, String> {
    Optional<VerifiedEmail> findByEmailAndSessionKey(
        final String email,
        final String sessionKey
    );
}
