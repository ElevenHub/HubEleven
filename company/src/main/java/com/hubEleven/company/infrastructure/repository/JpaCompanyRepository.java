package com.hubEleven.company.infrastructure.repository;

import com.hubEleven.company.domain.model.Company;
import java.util.UUID;

import com.hubEleven.company.domain.vo.CompanyStatus;
import com.hubEleven.company.domain.vo.CompanyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCompanyRepository extends JpaRepository<Company, UUID> {
	boolean existsByHubIdAndNameIgnoreCase(UUID hubId, String companyName);

	@Query(
			"SELECT c FROM Company c WHERE "
					+ "(:hubId IS NULL OR c.hubId = :hubId) "
					+ "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
					+ "AND (:type IS NULL OR c.companyType = :type) "
					+ "AND (:status IS NULL OR c.status = :status) "
					+ "AND c.deletedAt IS NULL")
	Page<Company> searchCompanies(
			@Param("hubId") UUID hubId,
			@Param("name") String name,
			@Param("type") CompanyType type,
			@Param("status") CompanyStatus status,
			Pageable pageable);
}
