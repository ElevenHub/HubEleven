package com.hubEleven.user.presentation.controller;

import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.user.application.AuthService;
import com.hubEleven.user.application.UserService;
import com.hubEleven.user.application.command.LoginCommand;
import com.hubEleven.user.application.command.UserCreateCommand;
import com.hubEleven.user.application.command.UserStatusUpdateCommand;
import com.hubEleven.user.application.command.UserUpdateCommand;
import com.hubEleven.user.application.dto.UserCreateResult;
import com.hubEleven.user.application.dto.UserInfoResult;
import com.hubEleven.user.infrastructure.security.CustomUserDetails;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
						user.name(),
						user.slackId(),
						user.role(),
						user.companyId());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(
			@Valid @RequestBody LoginRequest request) {
		LoginCommand command = new LoginCommand(request.username(), request.password());

		String token = authService.login(command);
		LoginResponse response = new LoginResponse(token);

		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.body(ApiResponse.success(response));
	}

	@GetMapping
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<ApiResponse<CommonPageResponse<UserInfoResponse>>> getAllUsers(
			@Valid CommonPageRequest pageRequest) {

		CommonPageResponse<UserInfoResult> result = userService.getAllUsers(pageRequest);
		CommonPageResponse<UserInfoResponse> response = toUserInfoResponsePage(result);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(
			@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
		var userInfo = userService.findUserById(id, userDetails.getUserId());

		UserInfoResponse response = UserInfoResponse.from(userInfo);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@PatchMapping("/{id}/status")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<ApiResponse<Void>> updateUserStatus(
			@PathVariable("id") Long id, @Valid @RequestBody UserStatusUpdateRequest request) {
		UserStatusUpdateCommand command = new UserStatusUpdateCommand(id, request.status());
		userService.updateUserStatus(command);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<CommonPageResponse<UserInfoResponse>>> searchUsers(
			@Valid CommonPageRequest pageRequest) {
		CommonPageResponse<UserInfoResult> result = userService.searchUsers(pageRequest);
		CommonPageResponse<UserInfoResponse> response = toUserInfoResponsePage(result);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('MASTER')")
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
		UserInfoResult userInfoResult = userService.updateUser(command);

		UserInfoResponse response = UserInfoResponse.from(userInfoResult);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<ApiResponse<Void>> deleteUser(
			@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
		userService.deleteUser(id, userDetails.getUserId());

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
	}

	private CommonPageResponse<UserInfoResponse> toUserInfoResponsePage(
			CommonPageResponse<UserInfoResult> result) {
		return new CommonPageResponse<>(
				result.content().stream().map(UserInfoResponse::from).toList(),
				result.page(),
				result.size(),
				result.totalElements(),
				result.totalPages(),
				result.first(),
				result.last());
	}
}
