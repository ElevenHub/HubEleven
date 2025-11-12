package com.hubEleven.company.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthHeaderFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			String userIdStr = request.getHeader("X-User-Id");
			String roleStr = request.getHeader("X-Role");
			String hubIdStr = request.getHeader("X-Hub-Id");
			String companyIdStr = request.getHeader("X-Company-Id");

			if (userIdStr != null && roleStr != null) {
				Long userId = Long.parseLong(userIdStr);
				UUID hubId = (hubIdStr != null && !hubIdStr.isBlank()) ? UUID.fromString(hubIdStr) : null;
				UUID companyId =
						(companyIdStr != null && !companyIdStr.isBlank())
								? UUID.fromString(companyIdStr)
								: null;
				Role role = Role.valueOf(roleStr);
				AuthUserContext.set(new AuthUser(userId, role, hubId, companyId));
			}
			chain.doFilter(request, response);
		} finally {
			AuthUserContext.clear();
		}
	}
}
