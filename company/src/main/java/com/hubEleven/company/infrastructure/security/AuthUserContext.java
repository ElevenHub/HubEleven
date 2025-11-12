package com.hubEleven.company.infrastructure.security;

public class AuthUserContext {
	private static final ThreadLocal<AuthUser> CONTEXT = new ThreadLocal<>();

	public static void set(AuthUser user) {
		CONTEXT.set(user);
	}

	public static AuthUser get() {
		return CONTEXT.get();
	}

	public static void clear() {
		CONTEXT.remove();
	}
}
