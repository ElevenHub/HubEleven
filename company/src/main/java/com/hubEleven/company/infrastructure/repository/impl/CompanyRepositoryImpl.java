package com.hubEleven.company.infrastructure.repository.impl;

import com.hubEleven.company.application.command.SearchCompanyCommand;
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.infrastructure.repository.JpaCompanyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<Company> search(SearchCompanyCommand cmd, Pageable pageable) {
		return jpaCompanyRepository.searchCompanies(
				cmd.hubId(), cmd.name(), cmd.type(), cmd.status(), pageable);
	}
}
