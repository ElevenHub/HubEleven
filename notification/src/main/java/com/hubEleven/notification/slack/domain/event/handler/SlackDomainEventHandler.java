package com.hubEleven.notification.slack.domain.event.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.slack.domain.event.SlackMessageSavedEvent;
import com.hubEleven.notification.slack.domain.model.SlackOutbox;
import com.hubEleven.notification.slack.domain.repository.SlackOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackDomainEventHandler {

	private final SlackOutboxRepository slackOutboxRepository;
	private final ObjectMapper objectMapper;

	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)

	public void handle(SlackMessageSavedEvent event) {
		try {
			String payload = objectMapper.writeValueAsString(event);
			SlackOutbox outbox = SlackOutbox.create(event.messageId(), payload);
			slackOutboxRepository.save(outbox);
			log.info("Slack Outbox 저장 완료 - messageId={}", event.messageId());
		} catch (Exception e) {
			log.error("Slack Outbox 저장 실패 - messageId={}", event.messageId(), e);
			throw new RuntimeException("Slack Outbox 저장 실패", e);
		}
	}
}
