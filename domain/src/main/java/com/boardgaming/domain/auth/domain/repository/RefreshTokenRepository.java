package com.boardgaming.domain.auth.domain.repository;

import com.boardgaming.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    boolean existsByValue(final String value);
    Optional<RefreshToken> findByValue(final String value);
}
