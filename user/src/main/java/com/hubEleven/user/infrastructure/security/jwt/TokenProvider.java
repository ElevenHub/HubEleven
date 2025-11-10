package com.hubEleven.user.infrastructure.security.jwt;

import com.hubEleven.user.infrastructure.security.CustomUserDetails;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider implements JwtProvider {

	private final JwtEncoder jwtEncoder;

	@Override
	public String generateToken(CustomUserDetails userDetails) {
		Instant now = Instant.now();

		String role =
				userDetails.getAuthorities().stream()
						.findFirst()
						.map(auth -> auth.getAuthority())
						.orElse("USER");

		JwtClaimsSet claims =
				JwtClaimsSet.builder()
						.subject(userDetails.getUsername())
						.issuedAt(now)
						.expiresAt(now.plusSeconds(1800L))
						.claim("role", role)
						.claim("userId", userDetails.getUserId())
						.claim("companyId", userDetails.getCompanyId())
						.build();

		JwsHeader header = JwsHeader.with(() -> "HS256").build();

		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}
}
