package com.hubEleven.user.infrastructure.security.jwt;

import com.hubEleven.user.infrastructure.security.CustomUserDetails;

public interface JwtProvider {

	String generateToken(CustomUserDetails userDetails);
}
