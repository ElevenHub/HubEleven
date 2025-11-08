package com.hubEleven.company.domain.repository;

import com.hubEleven.company.domain.model.CompanyStatus;
import com.hubEleven.company.domain.model.CompanyType;
import java.util.UUID;

public record CompanySearchCondition(
		UUID hubId, String companyName, CompanyType type, CompanyStatus status) {}
