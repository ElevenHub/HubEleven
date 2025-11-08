package com.hubEleven.company.application.service;

import static com.hubEleven.company.domain.exception.CompanyErrorCode.*;

import com.hubEleven.common.code.ErrorCode;
import com.hubEleven.common.request.CommonPageRequest;
import com.hubEleven.common.response.CommonPageResponse;
import com.hubEleven.common.utils.PagingUtils;
import com.hubEleven.company.application.dto.CompanyDTO;
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.model.CompanyStatus;
import com.hubEleven.company.domain.model.CompanyType;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.domain.repository.CompanySearchCondition;
import com.hubEleven.company.infrastructure.client.HubClient;
import com.hubEleven.company.presentation.request.CompanyRequests;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyAppService {

	private final CompanyRepository companyRepository;
	private final HubClient hubClient;

	private void assertHubExists(UUID hubId) {
		try {
			hubClient.getHub(hubId);
		} catch (feign.FeignException.NotFound e) {
			throw new com.hubEleven.common.exception.GlobalException(HUB_NOT_FOUND);
		} catch (feign.FeignException e) {
			throw new com.hubEleven.common.exception.GlobalException(ErrorCode.SERVER_ERROR);
		}
	}

	@Transactional
	public CompanyDTO createCompany(CompanyRequests.Create req) {
		assertHubExists(req.hubId());
		if (companyRepository.existsByHubIdAndName(req.hubId(), req.name())) {
			throw new com.hubEleven.common.exception.GlobalException(COMPANY_DUPLICATED);
		}
		Company company =
				Company.create(req.hubId(), req.name(), req.type(), req.slackId(), req.address());
		return CompanyDTO.from(companyRepository.save(company));
	}

	@Transactional
	public CompanyDTO updateCompany(UUID companyId, CompanyRequests.Update req) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(
								() -> new com.hubEleven.common.exception.GlobalException(COMPANY_NOT_FOUND));

		assertHubExists(company.getHubId());

		if (req.name() != null && !req.name().isBlank()) {
			boolean changed = !req.name().equalsIgnoreCase(company.getName());
			if (changed && companyRepository.existsByHubIdAndName(company.getHubId(), req.name())) {
				throw new com.hubEleven.common.exception.GlobalException(COMPANY_DUPLICATED);
			}
		}
		company.changeType(req.type());
		company.update(req.name(), req.address(), req.slackId());
		return CompanyDTO.from(company);
	}

	@Transactional(readOnly = true)
	public CompanyDTO getCompany(UUID companyId) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(
								() -> new com.hubEleven.common.exception.GlobalException(COMPANY_NOT_FOUND));
		return CompanyDTO.from(company);
	}

	@Transactional(readOnly = true)
	public CommonPageResponse<CompanyDTO> findCompanyList(CommonPageRequest pageReq) {
		var page =
				companyRepository.search(
						new CompanySearchCondition(null, null, null, null), pageReq.toPageable());
		return PagingUtils.convert(page, CompanyDTO::from);
	}

	@Transactional(readOnly = true)
	public CommonPageResponse<CompanyDTO> searchCompany(
			Optional<UUID> hubId,
			Optional<String> name,
			Optional<CompanyType> type,
			Optional<CompanyStatus> status,
			CommonPageRequest pageReq) {
		var cond =
				new CompanySearchCondition(
						hubId.orElse(null), name.orElse(null), type.orElse(null), status.orElse(null));
		var page = companyRepository.search(cond, pageReq.toPageable());
		return PagingUtils.convert(page, CompanyDTO::from);
	}

	@Transactional
	public CompanyDTO changeStatus(UUID companyId, String rawStatus) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(
								() -> new com.hubEleven.common.exception.GlobalException(COMPANY_NOT_FOUND));

		CompanyStatus newStatus;
		try {
			newStatus = CompanyStatus.valueOf(rawStatus.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new com.hubEleven.common.exception.GlobalException(
					com.hubEleven.common.code.ErrorCode.VALIDATION_ERROR); // 공통 코드
		}
		company.changeStatus(newStatus);
		return CompanyDTO.from(company);
	}

	@Transactional
	public void deleteCompany(UUID companyId, Long deleterId) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(
								() -> new com.hubEleven.common.exception.GlobalException(COMPANY_NOT_FOUND));
		company.delete(deleterId);
	}
}
