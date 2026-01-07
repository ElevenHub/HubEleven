package com.hubEleven.notification.slack.domain.service;

import com.hubEleven.notification.ai.application.dto.response.GenerateMessageResponse;
import com.hubEleven.notification.slack.application.dto.SlackMessageCreateRequest;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SlackMessageDomainService {

	private static final DateTimeFormatter DATETIME_FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public String formatMessage(SlackMessageCreateRequest req, GenerateMessageResponse aiResponse) {
		StringBuilder sb = new StringBuilder();

		sb.append("주문 번호 : ").append(req.orderId()).append("\n");
		sb.append("주문자 정보 : ")
				.append(req.customerName())
				.append(" / ")
				.append(req.customerEmail())
				.append("\n");
		sb.append("주문 시간 : ").append(formatDateTime(req.orderDateTime())).append("\n");
		sb.append("상품 정보 : ").append(formatItems(req.items())).append("\n");
		sb.append("요청 사항 : ").append(req.requestNote()).append("\n");
		sb.append("발송지 : ").append(req.sourceHub()).append("\n");
		sb.append("경유지 : ").append(formatViaHubs(req.viaHubs())).append("\n");
		sb.append("도착지 : ")
				.append(req.destinationHub())
				.append(" / ")
				.append(req.destinationAddress())
				.append("\n");
		sb.append("배송담당자 : ")
				.append(req.deliveryManagerName())
				.append(" / ")
				.append(req.deliveryManagerEmail())
				.append("\n\n");

		if (aiResponse != null && aiResponse.data() != null) {
			sb.append(aiResponse.data().messageBody());
		}

		return sb.toString();
	}

	private String formatItems(java.util.List<SlackMessageCreateRequest.Item> items) {
		if (items == null || items.isEmpty()) {
			return "정보 없음";
		}
		return items.stream()
				.map(item -> item.name() + " " + item.quantity() + "개")
				.collect(Collectors.joining("\n"));
	}

	private String formatViaHubs(java.util.List<String> viaHubs) {
		if (viaHubs == null || viaHubs.isEmpty()) {
			return "직행";
		}
		return String.join(", ", viaHubs);
	}

	private String formatDateTime(java.time.LocalDateTime dateTime) {
		if (dateTime == null) {
			return "정보 없음";
		}
		return dateTime.format(DATETIME_FORMATTER);
	}
}
