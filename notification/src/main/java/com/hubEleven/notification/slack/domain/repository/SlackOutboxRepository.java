package com.hubEleven.notification.slack.domain.repository;

import com.hubEleven.notification.slack.domain.model.SlackOutbox;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackOutboxRepository extends JpaRepository<SlackOutbox, UUID> {
	List<SlackOutbox> findByPublishedFalse();
}
