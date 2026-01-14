package com.hubEleven.notification.slack.infrastructure.repository;

import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSlackMessageRepository extends JpaRepository<SlackMessage, UUID> {
	Optional<SlackMessage> findFirstByOrderIdAndStatus(UUID orderId, SlackMessageStatus status);
}
