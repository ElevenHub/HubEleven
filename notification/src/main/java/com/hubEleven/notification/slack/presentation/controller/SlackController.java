package com.hubEleven.notification.slack.presentation.controller;

import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.notification.slack.application.dto.SlackMessageCreateRequest;
import com.hubEleven.notification.slack.application.dto.SlackMessageResponse;
import com.hubEleven.notification.slack.application.dto.SlackMessageUpdateRequest;
import com.hubEleven.notification.slack.application.service.SlackMessageAppService;
import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: 권한체크 - 로그인한 모든 사용자/ 내부 시스템은 발송 가능하도록 (create만)
@Tag(name = "Slack", description = "Slack 메시지 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/slack/messages")
public class SlackController {

	private final SlackMessageAppService slackMessageAppService;

	@Operation(summary = "메시지 발송 API", description = "AI를 통해 메시지를 생성하고 Slack으로 발송한다.")
	@PostMapping
	public ResponseEntity<ApiResponse<SlackMessageResponse>> createMessage(
			@Valid @RequestBody SlackMessageCreateRequest request) {
		SlackMessageResponse response = slackMessageAppService.createMessage(request);
		return ApiResponseEntity.success(response);
	}

	@Operation(summary = "메시지 수정 API", description = "기존 Slack 메시지를 수정합니다.")
	@PatchMapping("/{messageId}")
	public ResponseEntity<ApiResponse<SlackMessageResponse>> updateMessage(
			@PathVariable UUID messageId, @Valid @RequestBody SlackMessageUpdateRequest request) {
		SlackMessageResponse response = slackMessageAppService.updateMessage(messageId, request);
		return ApiResponseEntity.success(response);
	}

	@Operation(summary = "메시지 삭제 API", description = "Slack 메시지를 삭제한다.")
	@DeleteMapping("/{messageId}")
	public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
		slackMessageAppService.deleteMessage(messageId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "메시지 상세 조회 API", description = "특정 Slack 메시지의 상세 정보를 조회한다.")
	@GetMapping("/{messageId}")
	public ResponseEntity<ApiResponse<SlackMessageResponse>> getMessage(
			@PathVariable UUID messageId) {
		SlackMessageResponse response = slackMessageAppService.getMessage(messageId);
		return ApiResponseEntity.success(response);
	}

	@Operation(summary = "메시지 목록/검색 API", description = "조건에 따라 Slack 메시지 목록을 조회한다.")
	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<SlackMessageResponse>>> searchMessages(
			@RequestParam(required = false) SlackMessageStatus status,
			@RequestParam(required = false) String channel,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
					LocalDateTime dateFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
					LocalDateTime dateTo,
			@Valid CommonPageRequest pageReq) {
		var page = slackMessageAppService.searchMessages(status, channel, dateFrom, dateTo, pageReq);
		return ApiResponseEntity.success(page);
	}
}
