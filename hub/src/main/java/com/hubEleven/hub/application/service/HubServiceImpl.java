package com.hubEleven.hub.application.service;

import com.hubEleven.hub.application.command.CreateHubCommand;
import com.hubEleven.hub.application.command.DeleteHubCommand;
import com.hubEleven.hub.application.command.UpdateHubCommand;
import com.hubEleven.hub.application.dto.HubListResult;
import com.hubEleven.hub.application.dto.HubResult;
import com.hubEleven.hub.domain.model.Hub;
import com.hubEleven.hub.domain.repository.HubRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HubServiceImpl implements HubService {

	private final HubRepository hubRepository;

	public HubServiceImpl(HubRepository hubRepository) {
		this.hubRepository = hubRepository;
	}

	/*
	 *  1. 감사 로그 전체적으로 수정
	 *  2. 허브 등록 및 수정 시 위도 경도 외부 API 연결 -> 주소를 통해 해당 주소의 위도 경도 받아오기 (Kakao Local API)
	 *  3. Hub 위치 정보 수정 시 허브 경로 재배치
	 *  4. createdBy/updatedBy/deletedBy를 Gateway 완성 후 받아오기
	 *  5. Common 모듈의 예외로 교체
	 * */
	@Override
	public HubResult createHub(CreateHubCommand command) {
		// 중복 체크
		if (hubRepository.existsByName(command.name())) {
			throw new IllegalArgumentException("이미 동일한 이름의 허브가 존재합니다: " + command.name());
		}

		// 임시 값
		Long createdBy = 1L;

		Hub hub =
				Hub.create(
						command.name(),
						command.address(),
						command.latitude(),
						command.longitude(),
						command.regionCode(),
						createdBy);

		Hub saved = hubRepository.save(hub);
		return HubResult.from(saved);
	}

	@Override
	public HubResult updateHub(UpdateHubCommand command) {
		UUID hubId = command.hubId();
		Hub hub =
				hubRepository
						.findById(hubId)
						.orElseThrow(() -> new IllegalArgumentException("해당 허브를 찾을 수 없습니다: " + hubId));

		// 이름 변경 시 중복 체크
		if (command.name() != null && !command.name().equals(hub.getName())) {
			if (hubRepository.existsByName(command.name())) {
				throw new IllegalArgumentException("이미 동일한 이름의 허브가 존재합니다: " + command.name());
			}
		}

		// 임시 값
		Long updatedBy = 1L;

		// 부분 수정
		hub.update(
				command.name(),
				command.address(),
				command.latitude(),
				command.longitude(),
				command.regionCode(),
				updatedBy);

		// 더티체킹으로 반영
		return HubResult.from(hub);
	}

	@Override
	public void deleteHub(DeleteHubCommand command) {
		UUID hubId = command.hubId();
		Hub hub =
				hubRepository
						.findById(hubId)
						.orElseThrow(() -> new IllegalArgumentException("해당 허브를 찾을 수 없습니다: " + hubId));

		// 임시 값
		Long deletedBy = 1L;
		hub.softDelete(deletedBy);
		hubRepository.save(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public HubResult getHub(UUID hubId) {
		Hub hub =
				hubRepository
						.findById(hubId)
						.orElseThrow(() -> new IllegalArgumentException("해당 허브를 찾을 수 없습니다: " + hubId));
		return HubResult.from(hub);
	}

	@Override
	@Transactional(readOnly = true)
	public HubListResult getHubs() {
		// 삭제되지 않은 Hub만 조회
		List<Hub> hubs = hubRepository.findAllNotDeleted();

		return new HubListResult(hubs.stream().map(HubResult::from).toList());
	}
}
