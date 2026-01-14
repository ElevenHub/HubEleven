package com.hubEleven.notification.slack.domain.service;

import com.hubEleven.notification.ai.application.dto.response.GenerateMessageResponse;
import com.hubEleven.notification.slack.application.command.CreateSlackMessageCommand;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.hubEleven.notification.slack.application.command.CreateSlackMessageItemCommand;
import org.springframework.stereotype.Service;

@Service
public class SlackMessageDomainService {

	private static final DateTimeFormatter DATETIME_FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public String formatMessage(CreateSlackMessageCommand cmd, GenerateMessageResponse aiResponse) {
		StringBuilder sb = new StringBuilder();

		sb.append("주문 번호 : ").append(cmd.orderId()).append("\n");
		sb.append("주문자 정보 : ")
				.append(cmd.customerName())
				.append(" / ")
				.append(cmd.customerEmail())
				.append("\n");
		sb.append("주문 시간 : ").append(formatDateTime(cmd.orderDateTime())).append("\n");
		sb.append("상품 정보 : ").append(formatItems(cmd.items())).append("\n");
		sb.append("요청 사항 : ").append(nullSafe(cmd.requestNote())).append("\n");
		sb.append("발송지 : ").append(cmd.sourceHub()).append("\n");
		sb.append("경유지 : ").append(formatViaHubs(cmd.viaHubs())).append("\n");
		sb.append("도착지 : ")
				.append(cmd.destinationHub())
				.append(" / ")
				.append(cmd.destinationAddress())
				.append("\n");
		sb.append("배송담당자 : ")
				.append(cmd.deliveryManagerName())
				.append(" / ")
				.append(cmd.deliveryManagerEmail())
				.append("\n\n");

		if (aiResponse != null && aiResponse.data() != null) {
			sb.append(aiResponse.data().messageBody());
		}

		return sb.toString();
	}

	private String formatItems(List<CreateSlackMessageItemCommand> items) {
		if (items == null || items.isEmpty()) {
			return "정보 없음";
		}
		return items.stream()
				.map(i -> i.name() + " " + i.quantity() + "개")
				.collect(Collectors.joining("\n"));
	}

	private String formatViaHubs(List<String> viaHubs) {
		if (viaHubs == null || viaHubs.isEmpty()) {
			return "직행";
		}
		return String.join(", ", viaHubs);
	}

	private String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			return "정보 없음";
		}
		return dateTime.format(DATETIME_FORMATTER);
	}

	private String nullSafe(String v) {
		return (v == null || v.isBlank()) ? "없음" : v;
	}
}
