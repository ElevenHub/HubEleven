package com.hubEleven.notification.slack.presentation.controller;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.notification.slack.presentation.dto.request.SlackMessageCreateRequest;
import com.hubEleven.notification.slack.presentation.dto.response.SlackMessageResponse;
import com.hubEleven.notification.slack.presentation.dto.request.SlackMessageUpdateRequest;
import com.hubEleven.notification.slack.application.service.SlackService;
import com.hubEleven.notification.slack.exception.SlackMessageErrorCode;
import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;
import com.hubEleven.notification.slack.infrastructure.security.AuthUser;
import com.hubEleven.notification.slack.infrastructure.security.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Slack", description = "Slack 메시지 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/slack/messages")
public class SlackController {
	private final SlackService slackService;

	@Operation(summary = "메시지 발송 API", description = "AI를 통해 메시지를 생성하고 Slack으로 발송한다.")
	@PostMapping
	public ResponseEntity<ApiResponse<SlackMessageResponse>> createMessage(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String roleHeader,
			@RequestHeader(value = "X-Hub-Id", required = false) String hubIdHeader,
			@RequestHeader(value = "X-Company-Id", required = false) String companyIdHeader,
			@Valid @RequestBody SlackMessageCreateRequest request) {
		AuthUser authUser = resolveAuthUser(userId, roleHeader, hubIdHeader, companyIdHeader);
		SlackMessageResponse response = slackService.createMessage(authUser, request);
		return ApiResponseEntity.success(response);
	}

	@Operation(summary = "메시지 수정 API", description = "기존 Slack 메시지를 수정합니다.")
	@PatchMapping("/{messageId}")
	public ResponseEntity<ApiResponse<SlackMessageResponse>> updateMessage(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String roleHeader,
			@RequestHeader(value = "X-Hub-Id", required = false) String hubIdHeader,
			@RequestHeader(value = "X-Company-Id", required = false) String companyIdHeader,
			@PathVariable UUID messageId,
			@Valid @RequestBody SlackMessageUpdateRequest request) {
		AuthUser authUser = resolveAuthUser(userId, roleHeader, hubIdHeader, companyIdHeader);
		SlackMessageResponse response =
				slackService.updateMessage(authUser, messageId, request);
		return ApiResponseEntity.success(response);
	}

	@Operation(summary = "메시지 삭제 API", description = "Slack 메시지를 삭제한다.")
	@DeleteMapping("/{messageId}")
	public ResponseEntity<Void> deleteMessage(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String roleHeader,
			@RequestHeader(value = "X-Hub-Id", required = false) String hubIdHeader,
			@RequestHeader(value = "X-Company-Id", required = false) String companyIdHeader,
			@PathVariable UUID messageId) {
		AuthUser authUser = resolveAuthUser(userId, roleHeader, hubIdHeader, companyIdHeader);
		slackService.deleteMessage(authUser, messageId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "메시지 상세 조회 API", description = "특정 Slack 메시지의 상세 정보를 조회한다.")
	@GetMapping("/{messageId}")
	public ResponseEntity<ApiResponse<SlackMessageResponse>> getMessage(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String roleHeader,
			@RequestHeader(value = "X-Hub-Id", required = false) String hubIdHeader,
			@RequestHeader(value = "X-Company-Id", required = false) String companyIdHeader,
			@PathVariable UUID messageId) {
		AuthUser authUser = resolveAuthUser(userId, roleHeader, hubIdHeader, companyIdHeader);
		SlackMessageResponse response = slackService.getMessage(authUser, messageId);
		return ApiResponseEntity.success(response);
	}

	@Operation(summary = "메시지 목록/검색 API", description = "조건에 따라 Slack 메시지 목록을 조회한다.")
	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<SlackMessageResponse>>> searchMessages(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String roleHeader,
			@RequestHeader(value = "X-Hub-Id", required = false) String hubIdHeader,
			@RequestHeader(value = "X-Company-Id", required = false) String companyIdHeader,
			@RequestParam(required = false) SlackMessageStatus status,
			@RequestParam(required = false) String channel,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
					LocalDateTime dateFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
					LocalDateTime dateTo,
			@Valid CommonPageRequest pageReq) {
		AuthUser authUser = resolveAuthUser(userId, roleHeader, hubIdHeader, companyIdHeader);
		var page =
				slackService.searchMessages(authUser, status, channel, dateFrom, dateTo, pageReq);
		return ApiResponseEntity.success(page);
	}

	private AuthUser resolveAuthUser(
			Long userId, String roleHeader, String hubIdHeader, String companyIdHeader) {
		if (userId == null || !StringUtils.hasText(roleHeader)) {
			throw new GlobalException(SlackMessageErrorCode.UNAUTHORIZED);
		}

		Role role;
		try {
			role = Role.fromHeader(roleHeader);
		} catch (Exception ex) {
			log.warn("잘못된 역할 헤더 값 수신: {}", roleHeader, ex);
			throw new GlobalException(SlackMessageErrorCode.UNAUTHORIZED);
		}

		return new AuthUser(
				userId, role, parseUuidOrNull(hubIdHeader), parseUuidOrNull(companyIdHeader));
	}

	private UUID parseUuidOrNull(String raw) {
		if (!StringUtils.hasText(raw)) {
			return null;
		}
		try {
			return UUID.fromString(raw.trim());
		} catch (IllegalArgumentException ex) {
			log.warn("UUID 헤더 파싱 실패 - value: {}", raw, ex);
			return null;
		}
	}
}
