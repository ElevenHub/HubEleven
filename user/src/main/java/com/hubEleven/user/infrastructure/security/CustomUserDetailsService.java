package com.hubEleven.user.infrastructure.security;

import static com.hubEleven.user.domain.exception.UserErrorCode.NOT_FOUND_USER;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =
				userRepository
						.findByUsername(username)
						.orElseThrow(() -> new GlobalException(NOT_FOUND_USER));
		return new CustomUserDetails(user);
	}
}
