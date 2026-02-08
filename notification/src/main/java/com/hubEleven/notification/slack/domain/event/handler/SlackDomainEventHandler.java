package com.hubEleven.notification.slack.domain.event.handler;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.notification.slack.application.port.SlackClient;
import com.hubEleven.notification.slack.domain.event.SlackMessageSavedEvent;
import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.repository.SlackMessageRepository;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
import com.hubEleven.notification.slack.exception.SlackErrorCode;
import java.util.UUID;
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

	private final SlackMessageRepository slackMessageRepository;
	private final SlackClient slackClient;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handle(SlackMessageSavedEvent event) {
		UUID messageId = event.messageId();

		try {
			SlackMessage slackMessage =
					slackMessageRepository
							.findById(messageId)
							.orElseThrow(() -> new GlobalException(SlackErrorCode.SLACK_MESSAGE_NOT_FOUND));

			if (slackMessage.getStatus() == SlackMessageStatus.SENT) {
				log.info("슬랙 전송 스킵 - messageId={}", messageId);
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
				log.info(
						"슬랙 전송 성공 - messageId={}, channelId={}, ts={}",
						messageId,
						result.channelId(),
						result.ts());
			} else {
				slackMessage.markAsFailed();
				slackMessageRepository.save(slackMessage);
				log.warn("슬랙 전송 실패 - messageId={}, error={}", messageId, result.error());
			}

		} catch (Exception e) {
			slackMessageRepository
					.findById(messageId)
					.ifPresent(
							m -> {
								m.markAsFailed();
								slackMessageRepository.save(m);
							});
			log.error("슬랙 전송 처리 중 예외 발생 - messageId={}", messageId, e);
		}
	}
}
