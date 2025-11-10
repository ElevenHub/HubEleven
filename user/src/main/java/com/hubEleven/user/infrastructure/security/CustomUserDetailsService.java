package com.hubEleven.user.infrastructure.security;

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
						.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		return new CustomUserDetails(user);
	}
}
