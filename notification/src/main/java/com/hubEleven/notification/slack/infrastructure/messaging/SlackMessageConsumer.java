package com.hubEleven.notification.slack.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.slack.application.port.SlackClient;
import com.hubEleven.notification.slack.domain.event.SlackMessageSavedEvent;
import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.repository.SlackMessageRepository;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackMessageConsumer {

	private final SlackMessageRepository slackMessageRepository;
	private final SlackClient slackClient;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "slack-message-send", groupId = "${spring.kafka.consumer.group-id}")
	@Transactional
	public void consume(String message, Acknowledgment ack) {
		try {
			SlackMessageSavedEvent event = objectMapper.readValue(message, SlackMessageSavedEvent.class);
			UUID messageId = event.messageId();

			SlackMessage slackMessage =
					slackMessageRepository
							.findById(messageId)
							.orElseThrow(() -> new RuntimeException("Slack Message not found: " + messageId));

			if (slackMessage.getStatus() == SlackMessageStatus.SENT) {
				log.info("이미 전송된 메시지입니다. - messageId={}", messageId);
				ack.acknowledge();
				return;
			}

			String channel = slackMessage.getChannel();
			String text = slackMessage.getMessage();

			SlackClient.SlackSendResult result;
			if (channel != null && !channel.isBlank()) {
				result = slackClient.sendToChannel(channel, text);
			} else {
				String email = slackMessage.getRecipientId();
				result = slackClient.sendDmByEmail(email, text);
			}

			if (result.success()) {
				slackMessage.markAsSent();
				slackMessageRepository.save(slackMessage);
				log.info("슬랙 전송 성공 - messageId={}", messageId);
				ack.acknowledge();
			} else {
				log.warn("슬랙 전송 실패 - messageId={}, error={}", messageId, result.error());
				throw new RuntimeException("Slack send failed: " + result.error());
			}

		} catch (Exception e) {
			log.error("메시지 처리 중 오류 발생", e);
			throw new RuntimeException(e);
		}
	}
}
