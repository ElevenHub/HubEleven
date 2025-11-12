package com.hubEleven.user.presentation.controller;

import com.commonLib.common.code.SuccessCode;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.user.application.AuthService;
import com.hubEleven.user.application.UserService;
import com.hubEleven.user.application.command.LoginCommand;
import com.hubEleven.user.application.command.UserCreateCommand;
import com.hubEleven.user.application.command.UserStatusUpdateCommand;
import com.hubEleven.user.application.command.UserUpdateCommand;
import com.hubEleven.user.application.dto.UserCreateResult;
import com.hubEleven.user.application.dto.UserInfo;
import com.hubEleven.user.domain.vo.Role;
import com.hubEleven.user.presentation.dto.request.LoginRequest;
import com.hubEleven.user.presentation.dto.request.SignupRequest;
import com.hubEleven.user.presentation.dto.request.UserStatusUpdateRequest;
import com.hubEleven.user.presentation.dto.request.UserUpdateRequest;
import com.hubEleven.user.presentation.dto.response.LoginResponse;
import com.hubEleven.user.presentation.dto.response.SignupResponse;
import com.hubEleven.user.presentation.dto.response.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {

	private final UserService userService;
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<SignupResponse>> signup(
			@Valid @RequestBody SignupRequest request) {
		UserCreateCommand command =
				new UserCreateCommand(
						request.username(),
						request.password(),
						request.name(),
						request.slackId(),
						request.phoneNumber(),
						request.role(),
						request.companyId());

		UserCreateResult user = userService.createUser(command);

		SignupResponse response =
				new SignupResponse(
						user.userId(),
						user.username(),
						user.password(),
						user.name(),
						user.slackId(),
						user.role(),
						user.companyId());
		return ApiResponseEntity.create(SuccessCode.CREATED, "/v1/user/" + user.userId(), response);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(
			@Valid @RequestBody LoginRequest request) {
		LoginCommand command = new LoginCommand(request.username(), request.password());

		String token = authService.login(command);
		LoginResponse response = new LoginResponse(token);

		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.body(ApiResponseEntity.success(response).getBody());
	}

	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<UserInfoResponse>>> getAllUsers(
			@Valid CommonPageRequest pageRequest) {

		CommonPageResponse<UserInfo> result = userService.getAllUsers(pageRequest);

		CommonPageResponse<UserInfoResponse> response =
				new CommonPageResponse<>(
						result.content().stream().map(UserInfoResponse::from).toList(),
						result.page(),
						result.size(),
						result.totalElements(),
						result.totalPages(),
						result.first(),
						result.last());

		return ApiResponseEntity.success(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(
			@PathVariable("id") Long id,
			@RequestHeader("X-User-Id") Long requestUserId,
			@RequestHeader("X-User-Role") Role requestUserRole) {
		var userInfo = userService.findUserById(id, requestUserId, requestUserRole);

		UserInfoResponse response =
				new UserInfoResponse(
						userInfo.userId(),
						userInfo.username(),
						userInfo.name(),
						userInfo.slackId(),
						userInfo.phoneNumber(),
						userInfo.role(),
						userInfo.status(),
						userInfo.companyId());

		return ApiResponseEntity.success(response);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<ApiResponse<Void>> updateUserStatus(
			@PathVariable("id") Long id, @Valid @RequestBody UserStatusUpdateRequest request) {
		UserStatusUpdateCommand command = new UserStatusUpdateCommand(id, request.status());
		userService.updateUserStatus(command);

		return ApiResponseEntity.success(null);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<CommonPageResponse<UserInfoResponse>>> searchUsers(
			@Valid CommonPageRequest pageRequest) {
		CommonPageResponse<UserInfo> result = userService.searchUsers(pageRequest);

		CommonPageResponse<UserInfoResponse> response =
				new CommonPageResponse<>(
						result.content().stream().map(UserInfoResponse::from).toList(),
						result.page(),
						result.size(),
						result.totalElements(),
						result.totalPages(),
						result.first(),
						result.last());

		return ApiResponseEntity.success(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<UserInfoResponse>> updateUser(
			@PathVariable("id") Long id, @Valid @RequestBody UserUpdateRequest request) {
		UserUpdateCommand command =
				new UserUpdateCommand(
						id,
						request.name(),
						request.slackId(),
						request.phoneNumber(),
						request.role(),
						request.companyId());
		UserInfo userInfo = userService.updateUser(command);

		UserInfoResponse response = UserInfoResponse.from(userInfo);
		return ApiResponseEntity.success(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteUser(
			@PathVariable("id") Long id, @RequestHeader("X-User-Id") Long requestUserId) {
		userService.deleteUser(id, requestUserId);

		return ApiResponseEntity.success(null);
	}
}
