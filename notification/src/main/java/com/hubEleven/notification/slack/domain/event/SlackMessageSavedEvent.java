package com.hubEleven.notification.slack.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/*
	SlackMessage가 생성된 후, 커밋 이후 전송 처리를 트리거하기 위한 도메인 이벤트
	실제 전송은 AFTER_COMMIT 핸들러에서 수행
*/
public record SlackMessageSavedEvent(UUID messageId, LocalDateTime occurredAt) {
	public static SlackMessageSavedEvent of(UUID messageId) {
		return new SlackMessageSavedEvent(messageId, LocalDateTime.now());
	}
}
