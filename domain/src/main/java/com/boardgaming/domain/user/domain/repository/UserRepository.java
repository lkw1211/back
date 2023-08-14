package com.boardgaming.domain.user.domain.repository;

import com.boardgaming.domain.auth.domain.OAuth2Provider;
import com.boardgaming.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(final String email);
	boolean existsByName(final String name);
	boolean existsByEmail(final String email);
	Optional<User> findByEmailAndProvider(
		final String email,
		final OAuth2Provider provider
	);
}
