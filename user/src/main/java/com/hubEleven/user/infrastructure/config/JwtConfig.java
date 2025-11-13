package com.hubEleven.user.infrastructure.config;

import com.hubEleven.user.infrastructure.security.jwt.CustomJwtAuthenticationConverter;
import com.hubEleven.user.infrastructure.security.jwt.TokenProvider;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class JwtConfig {

	@Value("${JWT_SECRET}")
	private String secretKey;

	@Bean
	JwtEncoder jwtEncoder() throws Exception {
		SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		return new NimbusJwtEncoder(new ImmutableSecret<>(key));
	}

	@Bean
	JwtDecoder jwtDecoder() throws Exception {
		SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		return NimbusJwtDecoder.withSecretKey(key).build();
	}

	@Bean
	TokenProvider tokenProvider() throws Exception {
		return new TokenProvider(jwtEncoder());
	}

	@Bean
	JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() throws Exception {
		var converter = new JwtGrantedAuthoritiesConverter();
		converter.setAuthoritiesClaimName("role");
		converter.setAuthorityPrefix("ROLE_");

		return converter;
	}

	@Bean
	CustomJwtAuthenticationConverter customJwtAuthenticationConverter(
			UserDetailsService userDetailsService,
			JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter) {
		return new CustomJwtAuthenticationConverter(userDetailsService, jwtGrantedAuthoritiesConverter);
	}
}
