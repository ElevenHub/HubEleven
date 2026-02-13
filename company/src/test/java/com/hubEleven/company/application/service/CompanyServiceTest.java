package com.hubEleven.company.application.service;

import com.hubEleven.company.application.command.CreateCompanyCommand;
import com.hubEleven.company.application.dto.response.CompanyResult;
import com.hubEleven.company.domain.vo.CompanyType;
import com.hubEleven.company.infrastructure.client.HubClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.util.UUID;

import static org.mockito.BDDMockito.given;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.cache.type=none",
                "logging.level.com.zaxxer.hikari=INFO"
        }
)
@ActiveProfiles("test")
class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @MockBean
    private HubClient hubClient;

    @BeforeEach
    void setUp() {
        given(hubClient.getHub(ArgumentMatchers.any(UUID.class)))
                .willAnswer(invocation -> {
                    UUID hubId = invocation.getArgument(0, UUID.class);
                    return new HubClient.HubDTO(hubId, "DUMMY-HUB", "DUMMY-ADDRESS");
                });
    }

    @Test
    @DisplayName("업체 단건 조회 성능 테스트 - DB 베이스라인")
    void benchmarkGetCompany_dbBaseline() {
        // 1) 더미 데이터 생성
        UUID hubId = UUID.randomUUID();
        CreateCompanyCommand command = new CreateCompanyCommand(
                hubId, "허브소속업체", CompanyType.PRODUCER, "slackId", "서울시 강남구"
        );

        CompanyResult created = companyService.createCompany(command, 1L, "MASTER");
        UUID targetId = created.companyId();

        // 2) 워밍업 (JIT + 초기 준비)
        for (int i = 0; i < 20; i++) {
            companyService.getCompany(targetId);
        }

        // 3) 성능 측정
        int loopCount = 1000;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < loopCount; i++) {
            companyService.getCompany(targetId);
        }

        stopWatch.stop();

        // 4) 결과 출력
        double totalMs = stopWatch.getTotalTimeMillis();
        double avgMs = totalMs / loopCount;

        System.out.println("==========================================");
        System.out.println(" [DB 베이스라인 성능 측정] - 반복 횟수: " + loopCount + "회");
        System.out.println(" 총 소요 시간: " + String.format("%.3f", totalMs) + " ms");
        System.out.println(" 평균 응답 시간: " + String.format("%.4f", avgMs) + " ms");
        System.out.println("==========================================");
    }
}
