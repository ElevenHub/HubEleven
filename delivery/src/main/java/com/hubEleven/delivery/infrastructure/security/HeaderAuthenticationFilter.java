package com.hubEleven.delivery.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

	private static final String USER_ID_HEADER = "X-User-Id";
	private static final String USER_ROLE_HEADER = "X-User-Role";

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String userId = request.getHeader(USER_ID_HEADER);
		String userRole = request.getHeader(USER_ROLE_HEADER);

		if (userId != null && userRole != null) {
			List<SimpleGrantedAuthority> authorities =
					Collections.singletonList(new SimpleGrantedAuthority(userRole));

			UsernamePasswordAuthenticationToken auth =
					new UsernamePasswordAuthenticationToken(userId, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		// Gateway 공개 경로면 스킵
		String path = request.getRequestURI();
		return path.startsWith("/public") || path.contains("/health");
	}
}
