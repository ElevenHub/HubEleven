package com.hubEleven.company.application.command;

import com.hubEleven.company.domain.model.CompanyType;

import java.util.UUID;

public record CreateCompanyCommand(
        UUID hubId,
        String name,
        CompanyType type,
        String slackId,
        String address
) {
}
