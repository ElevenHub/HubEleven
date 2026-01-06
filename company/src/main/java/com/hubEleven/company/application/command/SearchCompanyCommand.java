package com.hubEleven.company.application.command;

import com.hubEleven.company.domain.vo.CompanyStatus;
import com.hubEleven.company.domain.vo.CompanyType;

import java.util.UUID;

public record SearchCompanyCommand(
        UUID hubId,
        String name,
        CompanyType type,
        CompanyStatus status
) {
}
