package com.hubEleven.delivery;

import com.hubEleven.delivery.infrastructure.client.CompanyFeignClient;
import com.hubEleven.delivery.infrastructure.client.DeliveryManagerFeignClient;
import com.hubEleven.delivery.infrastructure.service.HubRouteFeignService;
import com.hubEleven.delivery.infrastructure.service.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
public class DeliveryApplicationTests {

	@Autowired private CompanyFeignClient companyFeignClient;
	@Autowired private DeliveryManagerFeignClient deliveryManagerFeignClient;
	@Autowired private HubRouteFeignService hubRouteFeignService;
	@Autowired private OrderFeignService orderFeignService;

	//    @Test
	//    void testCompany(){
	//        UUID companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
	//
	//        // WireMock에 가짜 응답 등록
	//        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/v1/companies/.*"))
	//                .willReturn(WireMock.aResponse()
	//                        .withHeader("Content-Type", "application/json")
	//                        .withBody("{ \"companyId\": \"" + companyId + "\", \"hubId\":
	// \"9abcdef0-1a2b-3c4d-5e6f-7a8b9c0d1e2f\" }")
	//                        .withStatus(200)));
	//
	//        // FeignClient 실제 호출 (WireMock 서버 호출)
	//        ApiResponse<CompanyFeignResponseDto> response =
	// companyFeignClient.getCompanyInfo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
	//        System.out.println(response);
	//        System.out.println(response.result().companyId());
	//        System.out.println(response.result().hubId());
	//
	//        // 응답 검증
	//        assertEquals(companyId.toString(), response.result().companyId().toString());
	//    }

	//    todo 배송 매니저
	//    @Test
	//    void testDeliveryManager(){
	//
	//        // WireMock에 가짜 응답 등록
	//        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/v1/companies/.*"))
	//                .willReturn(WireMock.aResponse()
	//                        .withHeader("Content-Type", "application/json")
	//                        .withBody("{ \"deliveryManager\":
	// \"9abcdef0-1a2b-3c4d-5e6f-7a8b9c0d1e2f\" }")
	//                        .withStatus(200)));
	//
	//        // FeignClient 실제 호출 (WireMock 서버 호출)
	//        UUID orderId = UUID.fromString("7ba070db-2f9c-45a8-b2a2-d60119d3e122");
	//        UUID toHubId = UUID.fromString("20202020-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
	//        ApiResponse<DeliveryManagerFeignResponseDto> companyManger
	//                = deliveryManagerFeignClient.getDeliveryManager(orderId, toHubId,
	// DeliveryType.COMPANY);
	//        ApiResponse<DeliveryManagerFeignResponseDto> hubManager
	//                = deliveryManagerFeignClient.getDeliveryManager(orderId, toHubId,
	// DeliveryType.COMPANY);
	//        System.out.println(companyManger);
	//        System.out.println(companyManger.result().deliveryManagerId());
	//        System.out.println(companyManger.result().deliveryOrder());
	//        System.out.println(hubManager);
	//        System.out.println(hubManager.result().deliveryManagerId());
	//        System.out.println(hubManager.result().deliveryOrder());
	//
	//        // 응답 검증
	//        assertEquals(orderId.toString(), companyManger.result().deliveryOrder().toString());
	//    }

	// 허브
	//		@Test
	//		void testHubRoute() {
	//			// WireMock에 가짜 응답 등록
	//			WireMock.stubFor(
	//					WireMock.get(WireMock.urlMatching("/v1/companies/.*"))
	//							.willReturn(
	//									WireMock.aResponse()
	//											.withHeader("Content-Type", "application/json")
	//											.withBody("{ \"route\": \"9abcdef0-1a2b-3c4d-5e6f-7a8b9c0d1e2f\" }")
	//											.withStatus(200)));
	//
	//			// FeignClient 실제 호출 (WireMock 서버 호출)
	//			UUID fromHubId = UUID.fromString("D231C350-D795-4D45-B0A2-C9DE55A9F2D2");
	//			UUID toHubId = UUID.fromString("B7639CD3-EC0B-4111-8AB8-8A36C1B2DC8B");
	//            ApiResponse<HubRouteFeignResponseDto> hubRouteFeign =
	//                    hubRouteFeignService.getRoute(fromHubId, toHubId);
	//            List<HubRouteSegmentResponseDto> hubRoute =
	// hubRouteFeign.result().segments().stream().toList();
	//			System.out.println(hubRoute);
	//		}

	// 주문
	//        @Test
	//        void testOrder(){
	//            // WireMock에 가짜 응답 등록
	//            WireMock.stubFor(
	//                    WireMock.get(WireMock.urlMatching("/v1/companies/.*"))
	//                            .willReturn(
	//                                    WireMock.aResponse()
	//                                            .withHeader("Content-Type", "application/json")
	//                                            .withBody("{ \"orderId\":
	// \"7ba070db-2f9c-45a8-b2a2-d60119d3e122\" }")
	//                                            .withStatus(200)));
	//
	//            // FeignClient 실제 호출 (WireMock 서버 호출)
	//            UUID orderId = UUID.fromString("7ba070db-2f9c-45a8-b2a2-d60119d3e122");
	//            ApiResponse<OrderFeignResponseDto> order = orderFeignService.getOrderInfo(orderId);
	//            System.out.println(order);
	//            System.out.println();
	//
	//            UUID fromCompanyId = order.result().requestorCompanyId(); // 요청업체
	//            UUID toCompanyId = order.result().recipientCompanyId(); // 수령 업체
	//
	//            System.out.println("요청업체: " + fromCompanyId);
	//            System.out.println("수령업체: " + toCompanyId);

	// 응답 검증
	//           assertEquals(fromCompanyId, order.result().orderId().toString());
	//        }

	// todo 유저
	//        @Test
	//        void testUSer(){
	//            // WireMock에 가짜 응답 등록
	//            WireMock.stubFor(
	//                    WireMock.get(WireMock.urlMatching("/v1/companies/.*"))
	//                            .willReturn(
	//                                    WireMock.aResponse()
	//                                            .withHeader("Content-Type", "application/json")
	//                                            .withBody("{ \"orderId\":
	// \"7ba070db-2f9c-45a8-b2a2-d60119d3e122\" }")
	//                                            .withStatus(200)));
	//        }
}
