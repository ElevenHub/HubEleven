package com.hubEleven.notification.slack.domain.model;

import com.commonLib.common.model.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_slack_outbox")
@NoArgsConstructor
public class SlackOutbox extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "outbox_id")
	private UUID id;

	@Column(name = "message_id", nullable = false)
	private UUID messageId;

	@Column(name = "payload", columnDefinition = "text", nullable = false)
	private String payload;

	@Column(name = "published")
	private boolean published = false;

	@Column(name = "published_at")
	private LocalDateTime publishedAt;

	public static SlackOutbox create(UUID messageId, String payload) {
		SlackOutbox outbox = new SlackOutbox();
		outbox.messageId = messageId;
		outbox.payload = payload;
		outbox.published = false;
		return outbox;
	}

	public void markAsPublished() {
		this.published = true;
		this.publishedAt = LocalDateTime.now();
	}
}
