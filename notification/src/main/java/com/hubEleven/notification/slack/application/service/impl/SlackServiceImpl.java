package com.hubEleven.notification.slack.application.service.impl;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.fasterxml.jackson.databind.JsonNode;
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

		SlackMessageContext context = buildSlackMessageContext(command);

		AiPayload aiPayload = findAiPayloadOrThrow(command.orderId());

		String formattedMessage = slackDomainService.formatMessage(context, aiPayload.messageBody());

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

	private AiPayload findAiPayloadOrThrow(UUID orderId) {
		return aiRequestLogRepository
				.findByOrderId(orderId)
				.map(
						logEntry -> {
							String messageBody = logEntry.getMessageBody();
							String finalDeadline = logEntry.getFinalDispatchDeadline();

							if (messageBody != null && !messageBody.isBlank()) {
								return new AiPayload(finalDeadline, messageBody);
							}

							String raw = logEntry.getRawResponse();
							if (raw == null || raw.isBlank()) {
								throw new GlobalException(AiErrorCode.AI_RESPONSE_PARSE_FAIL);
							}

							String cleaned = cleanJsonResponse(raw);

							try {
								JsonNode node = objectMapper.readTree(cleaned);

								String parsedDeadline = node.path("finalDispatchDeadline").asText(null);
								String parsedBody = node.path("messageBody").asText(null);

								if (parsedBody == null || parsedBody.isBlank()) {
									throw new GlobalException(AiErrorCode.AI_RESPONSE_PARSE_FAIL);
								}

								return new AiPayload(parsedDeadline, parsedBody);
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

	private SlackMessageContext buildSlackMessageContext(CreateSlackMessageCommand cmd) {
		List<String> viaHubs = (cmd.viaHubs() == null) ? Collections.emptyList() : cmd.viaHubs();

		List<SlackMessageItem> items =
				(cmd.items() == null)
						? Collections.emptyList()
						: cmd.items().stream()
								.map(
										it ->
												new SlackMessageItem(
														nullToEmpty(it.name()), it.quantity(), nullToEmpty(it.note())))
								.toList();

		return new SlackMessageContext(
				cmd.orderId(),
				blankToNull(cmd.customerName()),
				blankToNull(cmd.customerEmail()),
				cmd.orderDateTime(),
				blankToNull(cmd.sourceHub()),
				viaHubs,
				blankToNull(cmd.destinationHub()),
				blankToNull(cmd.destinationAddress()),
				blankToNull(cmd.requestNote()),
				blankToNull(cmd.deliveryManagerName()),
				blankToNull(cmd.deliveryManagerEmail()),
				items);
	}

	private String blankToNull(String s) {
		return (s == null || s.isBlank()) ? null : s;
	}

	private String nullToEmpty(String s) {
		return (s == null) ? "" : s;
	}

	private record AiPayload(String finalDispatchDeadline, String messageBody) {}
}
