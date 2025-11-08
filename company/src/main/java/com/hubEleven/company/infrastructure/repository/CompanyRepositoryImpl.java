package com.hubEleven.company.infrastructure.repository;

import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.domain.repository.CompanySearchCondition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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
public class CompanyRepositoryImpl implements CompanyRepository {

	private final JpaCompanyRepository jpaCompanyRepository;

	@PersistenceContext private EntityManager em;

	@Override
	@Transactional
	public Company save(Company company) {
		return jpaCompanyRepository.save(company);
	}

	@Override
	public Optional<Company> findById(UUID companyId) {
		return jpaCompanyRepository.findById(companyId);
	}

	@Override
	public boolean existsByHubIdAndName(UUID hubId, String companyName) {
		return jpaCompanyRepository.existsByHubIdAndNameIgnoreCase(hubId, companyName);
	}

	@Override
	public Page<Company> search(CompanySearchCondition condition, Pageable pageable) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Company> cq = cb.createQuery(Company.class);
		Root<Company> root = cq.from(Company.class);

		List<Predicate> predicates = buildPredicates(condition, cb, root);
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

		TypedQuery<Company> query = em.createQuery(cq);
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());
		List<Company> companies = query.getResultList();

		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Company> countRoot = countQuery.from(Company.class);
		List<Predicate> countPreds = buildPredicates(condition, cb, countRoot);
		countPreds.add(cb.isNull(countRoot.get("deletedAt")));
		countQuery.select(cb.count(countRoot)).where(countPreds.toArray(Predicate[]::new));
		Long total = em.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(companies, pageable, total);
	}

	private static List<Predicate> buildPredicates(
			CompanySearchCondition condition, CriteriaBuilder cb, Root<Company> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (condition != null) {
			if (condition.hubId() != null) {
				predicates.add(cb.equal(root.get("hubId"), condition.hubId()));
			}
			if (condition.companyName() != null && !condition.companyName().isBlank()) {
				String like = "%" + condition.companyName().trim().toLowerCase() + "%";
				predicates.add(cb.like(cb.lower(root.get("name")), like));
			}
			if (condition.type() != null) {
				predicates.add(cb.equal(root.get("companyType"), condition.type()));
			}
			if (condition.status() != null) {
				predicates.add(cb.equal(root.get("status"), condition.status()));
			}
		}
		return predicates;
	}
}
