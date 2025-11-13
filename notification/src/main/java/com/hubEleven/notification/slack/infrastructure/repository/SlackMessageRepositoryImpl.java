package com.hubEleven.notification.slack.infrastructure.repository;

import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;
import com.hubEleven.notification.slack.domain.repository.SlackMessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlackMessageRepositoryImpl implements SlackMessageRepository {

	private final JpaSlackMessageRepository jpaSlackMessageRepository;
	@PersistenceContext private EntityManager em;

	@Override
	@Transactional
	public SlackMessage save(SlackMessage slackMessage) {
		return jpaSlackMessageRepository.save(slackMessage);
	}

	@Override
	public Optional<SlackMessage> findById(UUID id) {
		return jpaSlackMessageRepository.findById(id);
	}

	@Override
	public Optional<SlackMessage> findFirstByOrderIdAndStatus(
			UUID orderId, SlackMessageStatus status) {
		return jpaSlackMessageRepository.findFirstByOrderIdAndStatus(orderId, status);
	}

	@Override
	public Page<SlackMessage> search(
			SlackMessageStatus status,
			String channel,
			LocalDateTime dateFrom,
			LocalDateTime dateTo,
			Pageable pageable) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SlackMessage> cq = cb.createQuery(SlackMessage.class);
		Root<SlackMessage> root = cq.from(SlackMessage.class);

		List<Predicate> predicates = buildPredicates(cb, root, status, channel, dateFrom, dateTo);
		predicates.add(cb.isNull(root.get("deletedAt")));
		cq.select(root).where(predicates.toArray(Predicate[]::new));

		if (pageable.getSort().isSorted()) {
			List<Order> orders = new ArrayList<>();
			for (Sort.Order o : pageable.getSort()) {
				orders.add(
						o.isAscending()
								? cb.asc(root.get(o.getProperty()))
								: cb.desc(root.get(o.getProperty())));
			}
			cq.orderBy(orders);
		}

		TypedQuery<SlackMessage> query = em.createQuery(cq);
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		List<SlackMessage> messages = query.getResultList();

		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<SlackMessage> countRoot = countQuery.from(SlackMessage.class);
		List<Predicate> countPreds = buildPredicates(cb, countRoot, status, channel, dateFrom, dateTo);
		countPreds.add(cb.isNull(countRoot.get("deletedAt")));
		countQuery.select(cb.count(countRoot)).where(countPreds.toArray(Predicate[]::new));
		Long total = em.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(messages, pageable, total);
	}

	private static List<Predicate> buildPredicates(
			CriteriaBuilder cb,
			Root<SlackMessage> root,
			SlackMessageStatus status,
			String channel,
			LocalDateTime dateFrom,
			LocalDateTime dateTo) {

		List<Predicate> predicates = new ArrayList<>();

		if (status != null) {
			predicates.add(cb.equal(root.get("status"), status));
		}

		if (channel != null && !channel.isBlank()) {
			predicates.add(cb.equal(root.get("channel"), channel));
		}

		if (dateFrom != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom));
		}

		if (dateTo != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), dateTo));
		}

		return predicates;
	}
}
