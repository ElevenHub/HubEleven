package com.hubEleven.gateWay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

	private final SecretKey secretKey;

	public JwtValidator(@Value("${JWT_SECRET}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public Claims validateToken(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}

	public boolean isTokenValid(String token) {
		try {
			validateToken(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String extractUsername(String token) {
		return validateToken(token).getSubject();
	}

	public String extractRole(String token) {
		return validateToken(token).get("role", String.class);
	}

	public Long extractUserId(String token) {
		return validateToken(token).get("userId", Long.class);
	}

	public String extractCompanyId(String token) {
		return validateToken(token).get("companyId", String.class);
	}
}
