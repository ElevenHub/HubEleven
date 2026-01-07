package com.hubEleven.notification.slack.application.service;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.ai.application.dto.response.GenerateMessageResponse;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import com.hubEleven.notification.ai.exception.NotificationErrorCode;
import com.hubEleven.notification.slack.application.dto.SlackMessageCreateRequest;
import com.hubEleven.notification.slack.application.dto.SlackMessageResponse;
import com.hubEleven.notification.slack.application.dto.SlackMessageUpdateRequest;
import com.hubEleven.notification.slack.domain.exception.SlackMessageErrorCode;
import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;
import com.hubEleven.notification.slack.domain.repository.SlackMessageRepository;
import com.hubEleven.notification.slack.domain.service.SlackMessageDomainService;
import com.hubEleven.notification.slack.infrastructure.client.SlackWebhookClient;
import com.hubEleven.notification.slack.infrastructure.security.AuthUser;
import com.hubEleven.notification.slack.infrastructure.security.Role;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlackMessageAppService {

	private final SlackMessageRepository slackMessageRepository;
	private final SlackWebhookClient slackWebhookClient;
	private final SlackMessageDomainService slackMessageDomainService;
	private final AiRequestLogRepository aiRequestLogRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	public SlackMessageResponse createMessage(AuthUser authUser, SlackMessageCreateRequest request) {
		assertCreateAccess(authUser);

		slackMessageRepository
				.findFirstByOrderIdAndStatus(request.orderId(), SlackMessageStatus.SENT)
				.ifPresent(
						m -> {
							throw new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_ALREADY_SENT);
						});

		GenerateMessageResponse aiResponse = findAiResultOrThrow(request.orderId());
		String formattedMessage = slackMessageDomainService.formatMessage(request, aiResponse);

		SlackMessage slackMessage =
				SlackMessage.create(
						request.orderId(), request.recipientId(), request.channel(), formattedMessage);

		SlackMessage savedSlackMessage = slackMessageRepository.save(slackMessage);
		sendToSlackAsync(savedSlackMessage.getId(), formattedMessage);

		return SlackMessageResponse.from(savedSlackMessage);
	}

	@Transactional
	public SlackMessageResponse updateMessage(
			AuthUser authUser, UUID messageId, SlackMessageUpdateRequest request) {
		assertUpdateAccess(authUser);

		SlackMessage slackMessage =
				slackMessageRepository
						.findById(messageId)
						.orElseThrow(() -> new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_NOT_FOUND));

		slackMessage.updateMessage(request.message());
		SlackMessage updatedSlackMessage = slackMessageRepository.save(slackMessage);

		return SlackMessageResponse.from(updatedSlackMessage);
	}

	@Transactional
	public void deleteMessage(AuthUser authUser, UUID messageId) {
		assertDeleteAccess(authUser);

		SlackMessage slackMessage =
				slackMessageRepository
						.findById(messageId)
						.orElseThrow(() -> new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_NOT_FOUND));

		slackMessage.delete(authUser.userId());
		slackMessageRepository.save(slackMessage);
	}

	public SlackMessageResponse getMessage(AuthUser authUser, UUID messageId) {
		assertReadAccess(authUser);

		SlackMessage slackMessage =
				slackMessageRepository
						.findById(messageId)
						.orElseThrow(() -> new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_NOT_FOUND));
		return SlackMessageResponse.from(slackMessage);
	}

	public CommonPageResponse<SlackMessageResponse> searchMessages(
			AuthUser authUser,
			SlackMessageStatus status,
			String channel,
			LocalDateTime dateFrom,
			LocalDateTime dateTo,
			CommonPageRequest pageReq) {
		assertReadAccess(authUser);

		var page =
				slackMessageRepository.search(status, channel, dateFrom, dateTo, pageReq.toPageable());
		return PagingUtils.convert(page, SlackMessageResponse::from);
	}

	private GenerateMessageResponse findAiResultOrThrow(UUID orderId) {
		var logEntry =
				aiRequestLogRepository
						.findByOrderId(orderId)
						.orElseThrow(() -> new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL));

		String raw = logEntry.getRawResponse();
		String cleaned = cleanJsonResponse(raw);

		try {
			var payload = objectMapper.readValue(cleaned, ResponsePayload.class);

			if (payload.finalDispatchDeadline == null || payload.finalDispatchDeadline.isBlank()) {
				throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
			}
			if (payload.messageBody == null || payload.messageBody.isBlank()) {
				throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
			}
			return GenerateMessageResponse.success(payload.finalDispatchDeadline, payload.messageBody);
		} catch (Exception e) {
			throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
		}
	}

	private String cleanJsonResponse(String rawJson) {
		if (rawJson == null || rawJson.isBlank()) return rawJson;
		String cleaned = rawJson.trim();
		if (cleaned.startsWith("")) cleaned = cleaned.substring(7);
		else if (cleaned.startsWith("```")) cleaned = cleaned.substring(3);
		if (cleaned.endsWith("```")) cleaned = cleaned.substring(0, cleaned.length() - 3);
		return cleaned.trim();
	}

	private void sendToSlackAsync(UUID messageId, String messageText) {
		String title = "배송 예상 시간 알림";

		slackWebhookClient
				.sendMessage(title, messageText)
				.subscribe(
						ok -> {
							if (Boolean.TRUE.equals(ok)) {
								updateMessageStatus(messageId, SlackMessageStatus.SENT);
								log.info("Slack message sent. id={}", messageId);
							} else {
								updateMessageStatus(messageId, SlackMessageStatus.FAILED);
								log.error("Slack webhook returned non-ok for id={}", messageId);
							}
						},
						error -> {
							updateMessageStatus(messageId, SlackMessageStatus.FAILED);
							log.error("Failed to send Slack message. id={}", messageId, error);
						});
	}

	@Transactional
	protected void updateMessageStatus(UUID messageId, SlackMessageStatus status) {
		slackMessageRepository
				.findById(messageId)
				.ifPresent(
						message -> {
							if (status == SlackMessageStatus.SENT) {
								message.markAsSent();
							} else if (status == SlackMessageStatus.FAILED) {
								message.markAsFailed();
							}
							slackMessageRepository.save(message);
						});
	}

	private void assertCreateAccess(AuthUser authUser) {
		requireAuthenticated(authUser);
	}

	private void assertUpdateAccess(AuthUser authUser) {
		requireRole(authUser, Role.MASTER);
	}

	private void assertDeleteAccess(AuthUser authUser) {
		requireRole(authUser, Role.MASTER);
	}

	private void assertReadAccess(AuthUser authUser) {
		requireRole(authUser, Role.MASTER);
	}

	private void requireAuthenticated(AuthUser authUser) {
		if (authUser == null) {
			throw new GlobalException(SlackMessageErrorCode.UNAUTHORIZED);
		}
	}

	private void requireRole(AuthUser authUser, Role requiredRole) {
		requireAuthenticated(authUser);
		if (authUser.role() != requiredRole) {
			throw new GlobalException(SlackMessageErrorCode.FORBIDDEN);
		}
	}

	private static final class ResponsePayload {
		public String finalDispatchDeadline;
		public String messageBody;
	}
}
