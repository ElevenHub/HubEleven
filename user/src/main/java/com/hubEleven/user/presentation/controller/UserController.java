package com.hubEleven.user.presentation.controller;

import com.commonLib.common.code.SuccessCode;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.hubEleven.user.application.AuthService;
import com.hubEleven.user.application.UserService;
import com.hubEleven.user.application.command.LoginCommand;
import com.hubEleven.user.application.command.UserCreateCommand;
import com.hubEleven.user.application.dto.UserCreateResult;
import com.hubEleven.user.presentation.dto.request.LoginRequest;
import com.hubEleven.user.presentation.dto.request.SignupRequest;
import com.hubEleven.user.presentation.dto.response.LoginResponse;
import com.hubEleven.user.presentation.dto.response.SignupResponse;
import com.hubEleven.user.presentation.dto.response.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUser(@PathVariable("id") Long id) {
		var userInfo = userService.findUserById(id);

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
}
