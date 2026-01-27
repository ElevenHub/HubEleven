package com.hubEleven.user.application;

import static com.hubEleven.user.domain.exception.UserErrorCode.*;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.hubEleven.user.application.command.UserCreateCommand;
import com.hubEleven.user.application.command.UserStatusUpdateCommand;
import com.hubEleven.user.application.command.UserUpdateCommand;
import com.hubEleven.user.application.dto.UserCreateResult;
import com.hubEleven.user.application.dto.UserInfoResult;
import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.repository.UserRepository;
import com.hubEleven.user.domain.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

	public UserInfoResult findUserById(Long id, Long requestUserId) {
		if (!id.equals(requestUserId)) {
			throw new GlobalException(UNAUTHORIZED_ACCESS);
		}

		User user =
				userRepository
						.findByIdAndNotDeleted(id)
						.orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

		return UserInfoResult.from(user);
	}

	@Transactional
	public void updateUserStatus(UserStatusUpdateCommand command) {
		User user =
				userRepository
						.findByIdAndNotDeleted(command.userId())
						.orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

		user.updateStatus(command.status());
		userRepository.save(user);
	}

	public CommonPageResponse<UserInfoResult> getAllUsers(CommonPageRequest pageRequest) {
		Page<User> users = userRepository.searchUsers(null, pageRequest.toPageable());
		return PagingUtils.convert(users, UserInfoResult::from);
	}

	public CommonPageResponse<UserInfoResult> searchUsers(CommonPageRequest pageRequest) {
		Page<User> users = userRepository.searchUsers(pageRequest.keyword(), pageRequest.toPageable());
		return PagingUtils.convert(users, UserInfoResult::from);
	}

	@Transactional
	public UserInfoResult updateUser(UserUpdateCommand command) {
		User user =
				userRepository
						.findByIdAndNotDeleted(command.userId())
						.orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

		user.updateInfo(
				command.name(),
				command.slackId(),
				command.phoneNumber(),
				command.role(),
				command.companyId());
		userRepository.save(user);
		return UserInfoResult.from(user);
	}

	@Transactional
	public void deleteUser(Long userId, Long deletedBy) {
		User user =
				userRepository
						.findByIdAndNotDeleted(userId)
						.orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

		user.delete(deletedBy);
		userRepository.save(user);
	}

	private void validateUsername(String username) {
		if (userRepository.existsByUsername(username)) {
			throw new GlobalException(DUPLICATED_USERNAME);
		}
	}
}
