package com.hubEleven.company.domain.repository;

import com.hubEleven.company.domain.vo.CompanyStatus;
import com.hubEleven.company.domain.vo.CompanyType;
import java.util.UUID;

public record CompanySearchCondition(
		UUID hubId, String companyName, CompanyType type, CompanyStatus status) {}
