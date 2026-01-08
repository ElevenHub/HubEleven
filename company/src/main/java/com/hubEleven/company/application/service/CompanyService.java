package com.hubEleven.company.application.service;

import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.company.application.command.ChangeCompanyStatusCommand;
import com.hubEleven.company.application.command.CreateCompanyCommand;
import com.hubEleven.company.application.command.SearchCompanyCommand;
import com.hubEleven.company.application.command.UpdateCompanyCommand;
import com.hubEleven.company.application.dto.response.CompanyResult;
import java.util.UUID;

public interface CompanyService {

	CompanyResult createCompany(CreateCompanyCommand cmd, Long userId, String userRole);

	CompanyResult updateCompany(UpdateCompanyCommand cmd, Long userId, String userRole);

	CompanyResult getCompany(UUID companyId);

	CommonPageResponse<CompanyResult> searchCompany(
			SearchCompanyCommand cmd, CommonPageRequest pageReq);

	CompanyResult changeStatus(ChangeCompanyStatusCommand cmd, Long userId, String userRole);

	void deleteCompany(UUID companyId, Long userId, String userRole);
}
