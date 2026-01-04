package com.hubEleven.notification.ai.application.service.impl;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.notification.ai.application.command.GenerateDispatchCommand;
import com.hubEleven.notification.ai.application.dto.response.GenerateMessageResponse;
import com.hubEleven.notification.ai.application.port.DispatchAiPort;
import com.hubEleven.notification.ai.application.service.AiService;
import com.hubEleven.notification.ai.application.service.GenerateDispatchService;
import com.hubEleven.notification.ai.domain.model.AiRequestLog;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import com.hubEleven.notification.ai.domain.service.PromptService;
import com.hubEleven.notification.ai.domain.vo.DispatchContext;
import com.hubEleven.notification.ai.domain.vo.DispatchResult;
import com.hubEleven.notification.ai.domain.vo.RequestStatus;
import com.hubEleven.notification.ai.exception.NotificationErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateDispatchServiceImpl implements GenerateDispatchService {

	private final AiRequestLogRepository aiRequestLogRepository;
	private final PromptService promptService;
	private final DispatchAiPort dispatchAiPort;
	private final AiService aiService;

	private static final int MAX_RETRY = 3;
	private static final long SLEEP_MILLIS = 2000L;

	@Override
	@Transactional(readOnly = true)
	public GenerateMessageResponse generate(GenerateDispatchCommand command) {

		AiRequestLog existing = aiRequestLogRepository.findByOrderId(command.orderId()).orElse(null);
		if (existing != null) {
			if (existing.getStatus() == RequestStatus.SUCCESS) {
				return GenerateMessageResponse.success(
						existing.getFinalDispatchDeadline(), existing.getMessageBody());
			}
			throw new GlobalException(NotificationErrorCode.AI_REQUEST_DUPLICATED);
		}

		DispatchContext ctx = toContext(command);

		String prompt = promptService.buildDispatchGuidancePrompt(ctx);

		String metadataText = toMetadata(command);

		AiRequestLog logRow = aiService.createRequested(command.orderId(), prompt, metadataText);

		for (int retry = 1; retry <= MAX_RETRY; retry++) {
			try {
				DispatchResult result = dispatchAiPort.generate(prompt);

				aiService.markSuccess(
						logRow.getId(),
						result.finalDispatchDeadline(),
						result.messageBody(),
						result.rawResponse(),
						metadataText);

				return GenerateMessageResponse.success(
						result.finalDispatchDeadline(), result.messageBody());

			} catch (GlobalException ge) {

				if (isRetryableByMessage(ge) && retry < MAX_RETRY) {
					log.warn(
							"AI retryable(GlobalException). retry={}/{} msg={}",
							retry,
							MAX_RETRY,
							ge.getMessage());
					aiService.markFail(logRow.getId(), "RETRY: " + safe(ge.getMessage()), metadataText);
					sleep();
					continue;
				}

				aiService.markFail(logRow.getId(), safe(ge.getMessage()), metadataText);
				throw ge;

			} catch (Exception ex) {

				if (isRetryableByCause(ex) && retry < MAX_RETRY) {
					log.warn("AI retryable(Exception). retry={}/{} ex={}", retry, MAX_RETRY, ex.toString());
					aiService.markFail(logRow.getId(), "RETRY: " + ex.toString(), metadataText);
					sleep();
					continue;
				}

				aiService.markFail(logRow.getId(), ex.toString(), metadataText);
				throw new GlobalException(NotificationErrorCode.AI_GENERATION_FAIL);
			}
		}

		throw new GlobalException(NotificationErrorCode.AI_GENERATION_FAIL);
	}

	private boolean isRetryableByMessage(GlobalException e) {
		String msg = safe(e.getMessage());

		return msg.equals(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE.getMessage())
				|| msg.equals(NotificationErrorCode.AI_RATE_LIMITED.getMessage())
				|| msg.contains(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE.getMessage())
				|| msg.contains(NotificationErrorCode.AI_RATE_LIMITED.getMessage());
	}

	private boolean isRetryableByCause(Throwable t) {
		if (t == null) return false;

		Throwable cur = t;
		while (cur != null) {
			String cn = cur.getClass().getName();

			if (cn.contains("Timeout")
					|| cn.contains("Connect")
					|| cn.contains("ReadTimeout")
					|| cn.contains("WriteTimeout")
					|| cn.contains("WebClientRequestException")
					|| cn.contains("IOException")) {
				return true;
			}

			cur = cur.getCause();
		}
		return false;
	}

	private void sleep() {
		try {
			Thread.sleep(SLEEP_MILLIS);
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}

	private DispatchContext toContext(GenerateDispatchCommand c) {
		List<DispatchContext.Item> items =
				c.items().stream()
						.map(i -> new DispatchContext.Item(i.name(), i.quantity(), i.note()))
						.toList();

		List<String> routes = c.viaHubs() == null ? List.of() : c.viaHubs();

		return new DispatchContext(
				c.orderId(),
				c.customerName(),
				c.customerEmail(),
				c.orderDateTime(),
				c.requestedArrivalDateTime(),
				c.sourceHub(),
				routes,
				c.destinationHub(),
				c.destinationAddress(),
				c.requestNote(),
				c.deliveryManagerName(),
				c.deliveryManagerEmail(),
				items);
	}

	private String toMetadata(GenerateDispatchCommand c) {
		return """
								orderId=%s
								customerName=%s
								customerEmail=%s
								orderDateTime=%s
								requestedArrivalDateTime=%s
								sourceHub=%s
								viaHubs=%s
								destinationHub=%s
								destinationAddress=%s
								requestNote=%s
								deliveryManagerName=%s
								deliveryManagerEmail=%s
								items=%s
								"""
				.formatted(
						c.orderId(),
						safe(c.customerName()),
						safe(c.customerEmail()),
						c.orderDateTime(),
						c.requestedArrivalDateTime(),
						safe(c.sourceHub()),
						c.viaHubs() == null ? "[]" : c.viaHubs(),
						safe(c.destinationHub()),
						safe(c.destinationAddress()),
						safe(c.requestNote()),
						safe(c.deliveryManagerName()),
						safe(c.deliveryManagerEmail()),
						c.items() == null ? "[]" : c.items())
				.trim();
	}

	private String safe(String s) {
		return s == null ? "" : s;
	}
}
