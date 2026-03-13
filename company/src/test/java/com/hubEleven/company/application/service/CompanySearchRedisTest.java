package com.hubEleven.company.application.service;

import com.commonLib.common.request.CommonPageRequest;
import com.hubEleven.company.application.command.CreateCompanyCommand;
import com.hubEleven.company.application.command.SearchCompanyCommand;
import com.hubEleven.company.domain.vo.CompanyType;
import com.hubEleven.company.infrastructure.client.HubClient;
import java.lang.reflect.Constructor;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import static org.mockito.BDDMockito.given;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.cache.type=redis",
                "spring.data.redis.host=localhost",
                "spring.data.redis.port=6379",
                "spring.cloud.config.enabled=false",
                "eureka.client.enabled=false"
        }
)
@ActiveProfiles("test")
class CompanySearchRedisTest {

    @Autowired
    CompanyService companyService;

    @MockitoBean
    HubClient hubClient;

    @BeforeEach
    void setUp() {
        given(hubClient.getHub(ArgumentMatchers.any(UUID.class)))
                .willAnswer(invocation -> {
                    UUID hubId = invocation.getArgument(0, UUID.class);
                    return new HubClient.HubDTO(hubId, "DUMMY-HUB", "DUMMY-ADDRESS");
                });
    }

    @Test
    @DisplayName("업체 검색 성능 테스트 - Redis 캐시 적용")
    void benchmarkSearchCompany_withRedisCache() {
        // 1) 더미 데이터 생성
        UUID hubId = UUID.randomUUID();
        for (int i = 0; i < 50; i++) {
            companyService.createCompany(
                    new CreateCompanyCommand(hubId, "업체-" + i, CompanyType.PRODUCER, "s" + i, "addr"),
                    1L, "MASTER"
            );
        }

        SearchCompanyCommand cmd = new SearchCompanyCommand(hubId, null, null, null);
        CommonPageRequest pageReq = createPageRequest(0, 20);

        // 2) 워밍업: 1회 미스(캐시 적재)
        companyService.searchCompany(cmd, pageReq);

        // 3) 성능 측정: 이후는 캐시 히트 기대
        int loopCount = 1000;
        StopWatch sw = new StopWatch();
        sw.start();
        for (int i = 0; i < loopCount; i++) {
            companyService.searchCompany(cmd, pageReq);
        }
        sw.stop();

        double totalMs = sw.getTotalTimeMillis();
        double avgMs = totalMs / loopCount;

        System.out.println("==========================================");
        System.out.println(" [Redis 캐시 성능 측정] - 반복 횟수: " + loopCount + "회");
        System.out.println(" 총 소요 시간: " + String.format("%.3f", totalMs) + " ms");
        System.out.println(" 평균 응답 시간: " + String.format("%.4f", avgMs) + " ms");
        System.out.println("==========================================");
    }

    /**
     * CommonPageRequest 생성자가 프로젝트마다 달라서(예: 5개 인자),
     * (int page, int size, ...) 형태 생성자를 찾아 나머지는 기본값(null/0/false)으로 채워 생성.
     */
    private static CommonPageRequest createPageRequest(int page, int size) {
        try {
            for (Constructor<?> ctor : CommonPageRequest.class.getConstructors()) {
                Class<?>[] types = ctor.getParameterTypes();

                // (int page, int size, ...) 찾기
                if (types.length >= 2 && types[0] == int.class && types[1] == int.class) {
                    Object[] args = new Object[types.length];
                    args[0] = page;
                    args[1] = size;

                    for (int i = 2; i < types.length; i++) {
                        Class<?> t = types[i];

                        if (t == String.class) args[i] = null;
                        else if (t == int.class || t == Integer.class) args[i] = 0;
                        else if (t == long.class || t == Long.class) args[i] = 0L;
                        else if (t == boolean.class || t == Boolean.class) args[i] = false;
                        else args[i] = null;
                    }

                    return (CommonPageRequest) ctor.newInstance(args);
                }
            }
            throw new IllegalStateException("CommonPageRequest에 (int page, int size, ...) 생성자가 없습니다.");
        } catch (Exception e) {
            throw new RuntimeException("CommonPageRequest 생성 실패", e);
        }
    }
}
