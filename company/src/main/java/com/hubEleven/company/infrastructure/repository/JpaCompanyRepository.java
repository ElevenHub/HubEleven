package com.hubEleven.company.infrastructure.repository;

import com.hubEleven.company.domain.model.Company;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {
	boolean existsByHubIdAndNameIgnoreCase(UUID hubId, String companyName);
}
