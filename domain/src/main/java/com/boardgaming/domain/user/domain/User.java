package com.boardgaming.domain.user.domain;

import com.boardgaming.domain.auth.domain.OAuth2Provider;
import com.boardgaming.domain.common.BaseTimeEntity;

import com.boardgaming.domain.user.dto.request.ChangeProfileRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	private String imageFileUrl;

	private String password;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	@Enumerated(value = EnumType.STRING)
	private OAuth2Provider provider;

	@Builder
	public User(
		final String name,
		final String email,
		final String password,
		final String imageFileUrl,
		final Role role,
		final OAuth2Provider provider
	) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.email = email;
		this.password = password;
		this.imageFileUrl = imageFileUrl;
		this.role = role;
		this.provider = provider;
	}

	public void regenerateId() {
		this.id = UUID.randomUUID().toString();
	}

	public void updatePassword(
		final String password
	) {
		this.password = password;
	}

	public void updateProfile(
		final ChangeProfileRequest request
	) {
		this.name = request.getName();
	}

	public User update(final String name, final String imageFileUrl) {
		this.name = name;
		this.imageFileUrl = imageFileUrl;

		return this;
	}

	public void updateImageFileUrl(
		final String imageFileUrl
	) {
		this.imageFileUrl = imageFileUrl;
	}
}
