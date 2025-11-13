package com.hubEleven.notification.ai.domain.service;

import com.commonLib.common.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.ai.application.dto.MessageGenerationRequest;
import com.hubEleven.notification.ai.domain.exception.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptDomainService {

	private final ObjectMapper objectMapper;

	public String buildDispatchGuidancePrompt(MessageGenerationRequest req) {
		String orderInfo;
		try {
			orderInfo = objectMapper.writeValueAsString(req);
		} catch (JsonProcessingException e) {
			throw new GlobalException(NotificationErrorCode.PROMPT_BUILD_FAIL);
		}

		return """
						아래 주문 정보를 참고해서 발송 허브 담당자가 참고할 안내 메시지를 작성해줘.
						필수 요구사항:
						1. 요청자가 원하는 도착 납기(`requestedArrivalDateTime`)와 배송 담당자 근무시간(09~18시)을 고려해
							납기를 맞추기 위한 `finalDispatchDeadline`을 먼저 계산.
						2. 결과는 반드시 JSON 하나의 객체로만 반환하고, 키는 두 개:
							{
								"finalDispatchDeadline": "최종 발송 시한을 한국어로 명확히 표기 (예: 8월 21일 오후 3시까지 발송 필요)",
								"messageBody": "Slack에 그대로 붙일 4~6줄 분량의 안내문. 존댓말 사용, 주문 요약/발송 시한/담당자 연락처 등을 포함."
							}
						3. 메시지에는 다음 내용을 포함:
							- 주문 ID, 고객/수령지 정보, 상품별 수량 요약
							- 경유 허브 정보(viaHubs가 비어있으면 '직행'이라고 명시)
							- 계산된 최종 발송 시한과 출고 준비 요청
							- 담당자 이름과 연락 이메일
						4. 코드 블록(````)이나 JSON 외 포맷은 절대 사용하지 말 것.

						주문 정보:
						%s
						"""
				.formatted(orderInfo);
	}
}
