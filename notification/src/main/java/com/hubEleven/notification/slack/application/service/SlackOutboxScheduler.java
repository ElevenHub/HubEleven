package com.hubEleven.notification.slack.application.service;

import com.hubEleven.notification.slack.domain.model.SlackOutbox;
import com.hubEleven.notification.slack.domain.repository.SlackOutboxRepository;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackOutboxScheduler {

	private final SlackOutboxRepository slackOutboxRepository;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private static final String TOPIC = "slack-message-send";

	@Scheduled(fixedDelay = 1000) // 1초마다 실행
	public void publishOutboxMessages() {
		List<SlackOutbox> unpublishedOutboxes = slackOutboxRepository.findByPublishedFalse();

		for (SlackOutbox outbox : unpublishedOutboxes) {
			try {
				// Kafka 발행 (동기 처리로 확실하게 전송 보장)
				kafkaTemplate
						.send(TOPIC, outbox.getMessageId().toString(), outbox.getPayload())
						.get(3, TimeUnit.SECONDS);

				// 발행 성공 시 상태 업데이트
				outbox.markAsPublished();
				slackOutboxRepository.save(outbox);
				log.info("Kafka 발행 및 Outbox 업데이트 성공 - messageId={}", outbox.getMessageId());

			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				log.error("Kafka 발행 실패 - messageId={}", outbox.getMessageId(), e);
				// 실패 시 다음 주기에 재시도 (DB 업데이트 하지 않음)
			} catch (Exception e) {
				log.error("Outbox 처리 중 알 수 없는 오류 - outboxId={}", outbox.getId(), e);
			}
		}
	}
}
