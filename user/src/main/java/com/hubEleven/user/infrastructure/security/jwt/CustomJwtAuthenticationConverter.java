package com.hubEleven.user.infrastructure.security.jwt;

import com.hubEleven.user.infrastructure.security.CustomUserDetails;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter
		implements Converter<Jwt, AbstractAuthenticationToken> {

	private final UserDetailsService userDetailsService;
	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		// JWT 클레임에서 사용자 식별자 추출
		String email = jwt.getSubject();

		// DB 에서 사용자 정보 조회
		CustomUserDetails userDetails =
				(CustomUserDetails) userDetailsService.loadUserByUsername(email);

		// 권한 변환
		Collection<GrantedAuthority> grantedAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt);

		return new UsernamePasswordAuthenticationToken(userDetails, jwt, grantedAuthorities);
	}
}
