package com.hubEleven.notification.ai.application.service.impl;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.notification.ai.application.service.AiService;
import com.hubEleven.notification.ai.domain.model.AiRequestLog;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import com.hubEleven.notification.ai.exception.AiErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiServiceImpl implements AiService {

	private final AiRequestLogRepository aiRequestLogRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public AiRequestLog createRequested(UUID orderId, String prompt, String metadataText) {
		try {
			return aiRequestLogRepository.save(AiRequestLog.requested(orderId, prompt, metadataText));
		} catch (DataIntegrityViolationException e) {
			throw new GlobalException(AiErrorCode.AI_REQUEST_DUPLICATED);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void markSuccess(
			UUID logId,
			String finalDispatchDeadline,
			String messageBody,
			String rawResponse,
			String metadataText) {
		AiRequestLog log =
				aiRequestLogRepository
						.findById(logId)
						.orElseThrow(() -> new GlobalException(AiErrorCode.AI_GENERATION_FAIL));

		log.success(finalDispatchDeadline, messageBody, rawResponse, metadataText);
		aiRequestLogRepository.save(log);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void markFail(UUID logId, String failureReason, String metadataText) {
		AiRequestLog log =
				aiRequestLogRepository
						.findById(logId)
						.orElseThrow(() -> new GlobalException(AiErrorCode.AI_GENERATION_FAIL));

		log.fail(failureReason, metadataText);
		aiRequestLogRepository.save(log);
	}
}
