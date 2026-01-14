package com.hubEleven.notification.slack.domain.repository;

import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SlackMessageRepository {
	SlackMessage save(SlackMessage slackMessage);

	Optional<SlackMessage> findById(UUID id);

	Page<SlackMessage> search(
			SlackMessageStatus status,
			String channel,
			LocalDateTime dateFrom,
			LocalDateTime dateTo,
			Pageable pageable);

	Optional<SlackMessage> findFirstByOrderIdAndStatus(UUID orderId, SlackMessageStatus status);
}
