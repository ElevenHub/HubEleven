package com.hubEleven.company.application.command;

import com.hubEleven.company.domain.model.CompanyStatus;
import com.hubEleven.company.domain.model.CompanyType;

import java.util.UUID;

public record ChangeStatusCompanyCommand(
        UUID companyId,
        CompanyStatus status
) {
}
