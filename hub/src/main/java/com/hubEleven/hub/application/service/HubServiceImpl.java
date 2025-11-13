package com.hubEleven.hub.application.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.hub.application.command.CreateHubCommand;
import com.hubEleven.hub.application.command.DeleteHubCommand;
import com.hubEleven.hub.application.command.UpdateHubCommand;
import com.hubEleven.hub.application.dto.HubListResult;
import com.hubEleven.hub.application.dto.HubResult;
import com.hubEleven.hub.common.exception.HubErrorCode;
import com.hubEleven.hub.domain.model.Hub;
import com.hubEleven.hub.domain.repository.HubRepository;
import com.hubEleven.hub.infrastructure.client.KakaoApiClient;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class HubServiceImpl implements HubService {

	private final HubRepository hubRepository;
	private final KakaoApiClient kakaoApiClient;

	public HubServiceImpl(HubRepository hubRepository, KakaoApiClient kakaoApiClient) {
		this.hubRepository = hubRepository;
		this.kakaoApiClient = kakaoApiClient;
	}

	@Override
	public HubResult createHub(CreateHubCommand command) {

		validateDuplicateName(command.name());

		Double latitude = command.latitude();
		Double longitude = command.longitude();

		if (latitude == null || longitude == null) {
			log.info("위도/경도 미입력, Kakao API로 geocoding 수행: address={}", command.address());
			Double[] coordinates = kakaoApiClient.getCoordinates(command.address());
			latitude = coordinates[0];
			longitude = coordinates[1];
		}

		Hub hub =
				Hub.create(
						command.name(),
						command.address(),
						latitude,
						longitude,
						command.regionCode(),
						command.userId());

		Hub saved = hubRepository.save(hub);
		return HubResult.from(saved);
	}

	@Override
	public HubResult updateHub(UpdateHubCommand command) {
		UUID hubId = command.hubId();
		Hub hub = findHubById(hubId);

		if (command.name() != null && !command.name().equals(hub.getName())) {
			validateDuplicateName(command.name());
		}

		// 부분 수정
		hub.update(
				command.name(),
				command.address(),
				command.latitude(),
				command.longitude(),
				command.regionCode(),
				command.userId());

		// 더티체킹으로 반영
		return HubResult.from(hub);
	}

	@Override
	public void deleteHub(DeleteHubCommand command) {
		UUID hubId = command.hubId();
		Hub hub = findHubById(hubId);

		hub.softDelete(command.userId());
		hubRepository.save(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public HubResult getHub(UUID hubId) {
		Hub hub = findHubById(hubId);
		return HubResult.from(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public HubListResult getHubs() {
		List<Hub> hubs = hubRepository.findAllNotDeleted();

		return new HubListResult(hubs.stream().map(HubResult::from).toList());
	}

	private Hub findHubById(UUID hubId) {
		return hubRepository
				.findById(hubId)
				.orElseThrow(() -> new GlobalException(HubErrorCode.HUB_NOT_FOUND));
	}

	private void validateDuplicateName(String name) {
		if (hubRepository.existsByName(name)) {
			throw new GlobalException(HubErrorCode.DUPLICATE_HUB_NAME);
		}
	}
}
