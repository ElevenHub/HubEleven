package com.hubEleven.user.application;

import static com.hubEleven.user.domain.exception.ErrorCode.DUPLICATED_USERNAME;
import static com.hubEleven.user.domain.exception.ErrorCode.FORBIDDEN_USER;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.user.application.command.UserCreateCommand;
import com.hubEleven.user.application.dto.UserCreateResult;
import com.hubEleven.user.application.dto.UserInfo;
import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserCreateResult createUser(UserCreateCommand command) {
		validateUsername(command.username());
		String encodedPassword = passwordEncoder.encode(command.password());

		User user =
				User.create(
						command.username(),
						encodedPassword,
						command.name(),
						command.slackId(),
						command.phoneNumber(),
						command.role(),
						command.companyId());

		userRepository.save(user);
		return UserCreateResult.from(user);
	}

	public UserInfo findUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new GlobalException(FORBIDDEN_USER));
		return UserInfo.from(user);
	}

	private void validateUsername(String username) {
		if (userRepository.existsByUsername(username)) {
			throw new GlobalException(DUPLICATED_USERNAME);
		}
	}
}
