package com.hubEleven.notification.ai.domain.service.impl;

import com.hubEleven.notification.ai.domain.service.PromptService;
import com.hubEleven.notification.ai.domain.vo.DispatchContext;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PromptServiceImpl implements PromptService {

	@Override
	public String buildDispatchGuidancePrompt(DispatchContext ctx) {

		String via =
				(ctx.viaHubs() == null || ctx.viaHubs().isEmpty())
						? "직행"
						: String.join(", ", ctx.viaHubs());

		String items =
				ctx.items().stream()
						.map(
								i ->
										"- "
												+ i.name()
												+ " "
												+ i.quantity()
												+ "개"
												+ (blank(i.note()) ? "" : " (" + i.note() + ")"))
						.collect(Collectors.joining("\n"));

		String note = blank(ctx.requestNote()) ? "-" : ctx.requestNote();

		return """
								아래 주문 정보를 참고해서 발송 허브 담당자가 참고할 안내 메시지를 작성해줘.
								필수 요구사항:
								1) 요청자가 원하는 도착 납기(`requestedArrivalDateTime`)와 배송 담당자 근무시간(09~18시)을 고려해
									납기를 맞추기 위한 `finalDispatchDeadline`을 먼저 계산.
								2) 결과는 반드시 JSON "하나의 객체"로만 반환하고, 키는 두 개:
									{
										"finalDispatchDeadline": "최종 발송 시한을 한국어로 명확히 표기",
										"messageBody": "Slack에 그대로 붙일 4~6줄 분량 안내문(존댓말)."
									}
								3) messageBody에는 주문 요약/경유지/최종 발송 시한/담당자 연락처를 포함.
								4) 코드블록(```) 및 JSON 외 텍스트는 절대 포함하지 말 것.

								주문 정보:
								- 주문 번호: %s
								- 주문자: %s / %s
								- 주문 시간: %s
								- 납기 요청: %s
								- 발송지: %s
								- 경유지: %s
								- 도착 허브: %s
								- 도착 주소: %s
								- 요청 사항: %s
								- 배송 담당자: %s / %s
								- 상품 목록:
								%s
								"""
				.formatted(
						ctx.orderId(),
						ctx.customerName(),
						ctx.customerEmail(),
						ctx.orderDateTime(),
						ctx.requestedArrivalDateTime(),
						ctx.sourceHub(),
						via,
						ctx.destinationHub(),
						ctx.destinationAddress(),
						note,
						ctx.deliveryManagerName(),
						ctx.deliveryManagerEmail(),
						items)
				.trim();
	}

	private boolean blank(String s) {
		return s == null || s.isBlank();
	}
}
