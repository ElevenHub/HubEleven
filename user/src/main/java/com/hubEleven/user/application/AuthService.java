package com.hubEleven.user.application;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.user.application.command.LoginCommand;
import com.hubEleven.user.infrastructure.security.CustomUserDetails;
import com.hubEleven.user.infrastructure.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import static com.hubEleven.user.domain.exception.ErrorCode.FAILED_LOGIN;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authManager;
	private final JwtProvider jwtProvider;

	public String login(LoginCommand command) {
		try {
			Authentication auth =
					authManager.authenticate(
							new UsernamePasswordAuthenticationToken(command.username(), command.password()));
			CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
			return jwtProvider.generateToken(user);
		} catch (AuthenticationException e) {
			throw new GlobalException(FAILED_LOGIN);
		}
	}
}
