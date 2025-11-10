package com.hubEleven.hub.infrastructure.config;

import com.hubEleven.hub.application.service.RouteService;
import com.hubEleven.hub.domain.model.Hub;
import com.hubEleven.hub.domain.repository.HubRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubDataInitializer implements CommandLineRunner {

	private final HubRepository hubRepository;
	private final RouteService routeService;

	@Override
	public void run(String... args) {
		// DB에 허브가 이미 존재하면 스킵
		List<Hub> existingHubs = hubRepository.findAll();
		if (!existingHubs.isEmpty()) {
			log.info("기존 허브 데이터가 존재하여 초기화를 건너뜁니다. (총 {}개)", existingHubs.size());
			return;
		}

		log.info("초기 허브 데이터 생성을 시작합니다.");

		try {
			createInitialHubs();
			log.info("초기 허브 데이터 생성 완료");

			log.info("허브 경로 생성을 시작합니다.");
			routeService.generateAllRoutes();
			log.info("허브 경로 생성 완료");

		} catch (Exception e) {
			log.error("초기 데이터 생성 중 오류 발생", e);
		}
	}

	private void createInitialHubs() {
		List<HubData> hubDataList =
				List.of(
						new HubData(
								"서울특별시 센터", "서울특별시 송파구 송파대로 55", 37.4742027808565, 127.123621185562, "SEOUL"),
						new HubData(
								"경기 북부 센터",
								"경기도 고양시 덕양구 권율대로 570",
								37.6403771056018,
								126.87379545786,
								"GYEONGGI_NORTH"),
						new HubData(
								"경기 남부 센터",
								"경기도 이천시 덕평로 257-21",
								37.1896213142136,
								127.375050006958,
								"GYEONGGI_SOUTH"),
						new HubData("부산광역시 센터", "부산 동구 중앙대로 206", 35.117605126596, 129.045060216345, "BUSAN"),
						new HubData("대구광역시 센터", "대구 북구 태평로 161", 35.8858, 128.5931, "DAEGU"),
						new HubData("인천광역시 센터", "인천 남동구 정각로 29", 37.4560499608337, 126.705255744089, "INCHEON"),
						new HubData("광주광역시 센터", "광주 서구 내방로 111", 35.1600994105234, 126.851461925213, "GWANGJU"),
						new HubData("대전광역시 센터", "대전 서구 둔산로 100", 36.3503849976553, 127.384633005948, "DAEJEON"),
						new HubData("울산광역시 센터", "울산 남구 중앙로 201", 35.5390270962011, 129.311356392207, "ULSAN"),
						new HubData(
								"세종특별자치시 센터", "세종특별자치시 한누리대로 2130", 36.4800579897497, 127.289039408864, "SEJONG"),
						new HubData(
								"강원특별자치도 센터", "강원특별자치도 춘천시 중앙로 1", 37.8800729197963, 127.727907820318, "GANGWON"),
						new HubData(
								"충청북도 센터", "충북 청주시 상당구 상당로 82", 36.6353867908159, 127.491428436987, "CHUNGBUK"),
						new HubData(
								"충청남도 센터", "충남 홍성군 홍북읍 충남대로 21", 36.6590416999343, 126.673057036952, "CHUNGNAM"),
						new HubData(
								"전북특별자치도 센터",
								"전북특별자치도 전주시 완산구 효자로 225",
								35.8194621650578,
								127.106396942356,
								"JEONBUK"),
						new HubData(
								"전라남도 센터", "전남 무안군 삼향읍 오룡길 1", 34.8174727676363, 126.465415935304, "JEONNAM"),
						new HubData(
								"경상북도 센터", "경북 안동시 풍천면 도청대로 455", 36.5761205474728, 128.505722686385, "GYEONGBUK"),
						new HubData(
								"경상남도 센터", "경남 창원시 의창구 중앙대로 300", 35.2378032514675, 128.691940442146, "GYEONGNAM"));

		Long createdBy = 1L; // 시스템 생성

		for (HubData data : hubDataList) {
			try {
				Hub hub =
						Hub.create(
								data.name, data.address, data.latitude, data.longitude, data.regionCode, createdBy);

				hubRepository.save(hub);
				log.info("허브 생성: {} ({})", data.name, data.address);

			} catch (Exception e) {
				log.error("허브 생성 실패: {} - {}", data.name, e.getMessage());
			}
		}
	}

	private record HubData(
			String name, String address, Double latitude, Double longitude, String regionCode) {}
}
