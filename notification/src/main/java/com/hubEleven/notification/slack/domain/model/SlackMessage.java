package com.hubEleven.notification.slack.domain.model;

import com.commonLib.common.model.BaseEntity;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
		name = "p_slack_message",
		indexes = {
			@Index(name = "idx_slack_receiver", columnList = "recipient_id"),
			@Index(name = "idx_slack_order", columnList = "order_id"),
			@Index(name = "idx_slack_status", columnList = "status")
		})
@NoArgsConstructor
public class SlackMessage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "slack_message_id")
	private UUID id;

	@Column(name = "order_id")
	private UUID orderId;

	@Column(name = "recipient_id", nullable = false, length = 100)
	private String recipientId;

	@Column(name = "channel", length = 100)
	private String channel;

	@Lob
	@Column(name = "message_text", columnDefinition = "text", nullable = false)
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	private SlackMessageStatus status = SlackMessageStatus.PENDING;

	@Column(name = "sent_at")
	private LocalDateTime sentAt;

	public static SlackMessage create(
			UUID orderId, String recipientId, String channel, String message) {
		SlackMessage slackMessage = new SlackMessage();
		slackMessage.orderId = orderId;
		slackMessage.recipientId = recipientId;
		slackMessage.channel = channel;
		slackMessage.message = message;
		slackMessage.status = SlackMessageStatus.PENDING;
		return slackMessage;
	}

	public void updateMessage(String message) {
		if (message != null && !message.isBlank()) {
			this.message = message;
		}
	}

	public void markAsSent() {
		this.status = SlackMessageStatus.SENT;
		this.sentAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}

	public void markAsFailed() {
		this.status = SlackMessageStatus.FAILED;
	}
}
