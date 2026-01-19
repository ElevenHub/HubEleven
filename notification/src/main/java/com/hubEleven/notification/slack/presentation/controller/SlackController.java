package com.hubEleven.notification.slack.presentation.controller;

import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.notification.slack.application.command.SearchSlackMessageCommand;
import com.hubEleven.notification.slack.application.dto.response.SlackMessageResult;
import com.hubEleven.notification.slack.application.service.SlackService;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
import com.hubEleven.notification.slack.presentation.dto.request.CreateSlackMessageRequest;
import com.hubEleven.notification.slack.presentation.dto.request.UpdateSlackMessageRequest;
import com.hubEleven.notification.slack.presentation.dto.response.SlackMessageResponse;
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
			@Valid @RequestBody CreateSlackMessageRequest req,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole) {
		log.info("Slack 메시지 발송 요청 - channel:{}", req.channel());

		SlackMessageResult result = slackService.createMessage(req.toCommand());
		log.info("Slack 메시지 발송 성공 - messageId:{}", result.messageId());

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(SlackMessageResponse.from(result)));
	}

	@Operation(summary = "메시지 수정 API", description = "기존 Slack 메시지를 수정합니다.")
	@PatchMapping("/{messageId}")
	public ResponseEntity<ApiResponse<SlackMessageResponse>> updateMessage(
			@PathVariable UUID messageId,
			@Valid @RequestBody UpdateSlackMessageRequest req,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole) {
		log.info("Slack 메시지 수정 요청 - messageId:{}", messageId);

		SlackMessageResult result = slackService.updateMessage(req.toCommand(messageId));
		log.info("Slack 메시지 수정 성공 - messageId:{}", messageId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(SlackMessageResponse.from(result)));
	}

	@Operation(summary = "메시지 삭제 API", description = "Slack 메시지를 삭제한다.")
	@DeleteMapping("/{messageId}")
	public ResponseEntity<ApiResponse<Object>> deleteMessage(
			@PathVariable UUID messageId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole) {
		log.info("Slack 메시지 삭제 요청 - messageId:{}", messageId);

		slackService.deleteMessage(messageId);
		log.info("Slack 메시지 삭제 성공 - messageId:{}", messageId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(summary = "메시지 상세 조회 API", description = "특정 Slack 메시지의 상세 정보를 조회한다.")
	@GetMapping("/{messageId}")
	public ResponseEntity<ApiResponse<SlackMessageResponse>> getMessage(
			@PathVariable UUID messageId) {
		log.debug("Slack 메시지 단건 조회 요청 - messageId:{}", messageId);

		SlackMessageResult result = slackService.getMessage(messageId);

		log.debug("Slack 메시지 단건 조회 성공 - messageId:{}", messageId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(SlackMessageResponse.from(result)));
	}

	@Operation(summary = "메시지 목록/검색 API", description = "조건에 따라 Slack 메시지 목록을 조회한다.")
	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<SlackMessageResponse>>> searchMessages(
			@Valid CommonPageRequest pageReq,
			@RequestParam(required = false) SlackMessageStatus status,
			@RequestParam(required = false) String channel,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
					LocalDateTime dateFrom,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
					LocalDateTime dateTo) {
		log.debug(
				"Slack 메시지 목록/검색 요청 - status:{}, channel:{}, dateFrom:{}, dateTo:{}, page:{}, size:{}",
				status,
				channel,
				dateFrom,
				dateTo,
				pageReq.page(),
				pageReq.size());

		var page =
				slackService.searchMessages(
						new SearchSlackMessageCommand(status, channel, dateFrom, dateTo), pageReq);

		var mapped =
				new CommonPageResponse<>(
						page.content().stream().map(SlackMessageResponse::from).toList(),
						page.page(),
						page.size(),
						page.totalElements(),
						page.totalPages(),
						page.first(),
						page.last());

		log.debug(
				"Slack 메시지 목록/검색 성공 - totalElements:{}, totalPages:{}",
				page.totalElements(),
				page.totalPages());

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(mapped));
	}
}
