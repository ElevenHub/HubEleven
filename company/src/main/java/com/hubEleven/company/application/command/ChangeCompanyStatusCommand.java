package com.hubEleven.company.application.command;

import com.hubEleven.company.domain.vo.CompanyStatus;

import java.util.UUID;

public record ChangeCompanyStatusCommand(
        UUID companyId,
        CompanyStatus status
) {
}
