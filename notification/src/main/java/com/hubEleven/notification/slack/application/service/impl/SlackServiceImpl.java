package com.hubEleven.notification.slack.application.service.impl;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import com.hubEleven.notification.ai.exception.AiErrorCode;
import com.hubEleven.notification.slack.application.command.CreateSlackMessageCommand;
import com.hubEleven.notification.slack.application.command.SearchSlackMessageCommand;
import com.hubEleven.notification.slack.application.command.UpdateSlackMessageCommand;
import com.hubEleven.notification.slack.application.dto.response.SlackMessageResult;
import com.hubEleven.notification.slack.application.service.SlackService;
import com.hubEleven.notification.slack.application.validator.SlackValidator;
import com.hubEleven.notification.slack.domain.event.SlackMessageSavedEvent;
import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.repository.SlackMessageRepository;
import com.hubEleven.notification.slack.domain.service.SlackDomainService;
import com.hubEleven.notification.slack.domain.vo.SlackMessageContext;
import com.hubEleven.notification.slack.domain.vo.SlackMessageItem;
import com.hubEleven.notification.slack.exception.SlackErrorCode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlackServiceImpl implements SlackService {

	private final SlackMessageRepository slackMessageRepository;
	private final SlackDomainService slackDomainService;
	private final AiRequestLogRepository aiRequestLogRepository;
	private final ObjectMapper objectMapper;
	private final SlackValidator slackValidator;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public SlackMessageResult createMessage(CreateSlackMessageCommand command) {
		slackValidator.slackCreate(command.orderId());

		ResponsePayload payload = findAiPayloadOrThrow(command.orderId());

		SlackMessageContext context = buildSlackMessageContext(command.orderId(), payload);

		String formattedMessage = slackDomainService.formatMessage(context, payload.messageBody);

		SlackMessage slackMessage =
				SlackMessage.create(
						command.orderId(), command.recipientId(), command.channel(), formattedMessage);

		SlackMessage saved = slackMessageRepository.save(slackMessage);

		eventPublisher.publishEvent(SlackMessageSavedEvent.of(saved.getId()));

		log.info("슬랙 메시지 저장 완료 - messageId={}", saved.getId());

		return SlackMessageResult.from(saved);
	}

	@Override
	@Transactional
	public SlackMessageResult updateMessage(UpdateSlackMessageCommand command) {
		slackValidator.slackUpdate();

		SlackMessage slackMessage =
				slackMessageRepository
						.findById(command.messageId())
						.orElseThrow(() -> new GlobalException(SlackErrorCode.SLACK_MESSAGE_NOT_FOUND));

		slackMessage.updateMessage(command.message());
		SlackMessage updated = slackMessageRepository.save(slackMessage);

		log.info("슬랙 메시지 수정 완료 - messageId={}", command.messageId());

		return SlackMessageResult.from(updated);
	}

	@Override
	@Transactional
	public void deleteMessage(UUID messageId) {
		slackValidator.slackDelete();

		slackMessageRepository
				.findById(messageId)
				.orElseThrow(() -> new GlobalException(SlackErrorCode.SLACK_MESSAGE_NOT_FOUND));

		throw new UnsupportedOperationException("삭제 로직을 연결하세요.");
	}

	@Override
	public SlackMessageResult getMessage(UUID messageId) {
		slackValidator.slackRead();

		SlackMessage slackMessage =
				slackMessageRepository
						.findById(messageId)
						.orElseThrow(() -> new GlobalException(SlackErrorCode.SLACK_MESSAGE_NOT_FOUND));

		return SlackMessageResult.from(slackMessage);
	}

	@Override
	public CommonPageResponse<SlackMessageResult> searchMessages(
			SearchSlackMessageCommand command, CommonPageRequest pageReq) {
		slackValidator.slackRead();

		return PagingUtils.convert(
				slackMessageRepository.search(
						command.status(),
						command.channel(),
						command.dateFrom(),
						command.dateTo(),
						pageReq.toPageable()),
				SlackMessageResult::from);
	}

	private ResponsePayload findAiPayloadOrThrow(UUID orderId) {
		return aiRequestLogRepository
				.findByOrderId(orderId)
				.map(
						logEntry -> {
							String raw = logEntry.getRawResponse();
							String cleaned = cleanJsonResponse(raw);

							try {
								ResponsePayload payload = objectMapper.readValue(cleaned, ResponsePayload.class);

								if (payload.finalDispatchDeadline == null
										|| payload.finalDispatchDeadline.isBlank()) {
									throw new GlobalException(AiErrorCode.AI_RESPONSE_PARSE_FAIL);
								}
								if (payload.messageBody == null || payload.messageBody.isBlank()) {
									throw new GlobalException(AiErrorCode.AI_RESPONSE_PARSE_FAIL);
								}
								return payload;
							} catch (Exception e) {
								throw new GlobalException(AiErrorCode.AI_RESPONSE_PARSE_FAIL);
							}
						})
				.orElseThrow(() -> new GlobalException(AiErrorCode.AI_RESPONSE_PARSE_FAIL));
	}

	private String cleanJsonResponse(String rawJson) {
		if (rawJson == null || rawJson.isBlank()) {
			return rawJson;
		}

		String cleaned = rawJson.trim();

		if (cleaned.startsWith("```json")) {
			cleaned = cleaned.substring(7);
		} else if (cleaned.startsWith("```")) {
			cleaned = cleaned.substring(3);
		}

		if (cleaned.endsWith("```")) {
			cleaned = cleaned.substring(0, cleaned.length() - 3);
		}

		return cleaned.trim();
	}

	private SlackMessageContext buildSlackMessageContext(UUID orderId, ResponsePayload payload) {
		LocalDateTime orderDateTime = parseLocalDateTime(payload.orderDateTime);

		List<String> viaHubs = (payload.viaHubs == null) ? Collections.emptyList() : payload.viaHubs;

		List<SlackMessageItem> items =
				(payload.items == null)
						? Collections.emptyList()
						: payload.items.stream()
								.map(
										it ->
												new SlackMessageItem(
														nullToEmpty(it.name), it.quantity, nullToEmpty(it.note)))
								.toList();

		return new SlackMessageContext(
				orderId,
				blankToNull(payload.customerName),
				blankToNull(payload.customerEmail),
				orderDateTime,
				blankToNull(payload.sourceHub),
				viaHubs,
				blankToNull(payload.destinationHub),
				blankToNull(payload.destinationAddress),
				blankToNull(payload.requestNote),
				blankToNull(payload.deliveryManagerName),
				blankToNull(payload.deliveryManagerEmail),
				items);
	}

	private LocalDateTime parseLocalDateTime(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return LocalDateTime.parse(value.trim());
		} catch (Exception ignore) {
			return null;
		}
	}

	private String blankToNull(String s) {
		return (s == null || s.isBlank()) ? null : s;
	}

	private String nullToEmpty(String s) {
		return (s == null) ? "" : s;
	}

	private static final class ResponsePayload {
		public String finalDispatchDeadline;
		public String messageBody;

		public String customerName;
		public String customerEmail;
		public String orderDateTime;
		public String sourceHub;
		public List<String> viaHubs;
		public String destinationHub;
		public String destinationAddress;
		public String requestNote;
		public String deliveryManagerName;
		public String deliveryManagerEmail;
		public List<ItemPayload> items;
	}

	private static final class ItemPayload {
		public String name;
		public int quantity;
		public String note;
	}
}
