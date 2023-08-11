package com.boardgaming.domain.user.domain;

public enum Role {
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	private String authority;

	Role(final String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}
}
