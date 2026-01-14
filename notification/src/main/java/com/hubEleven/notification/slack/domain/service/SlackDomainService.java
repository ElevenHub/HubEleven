package com.hubEleven.notification.slack.domain.service;

import com.hubEleven.notification.slack.domain.vo.SlackMessageContext;
import com.hubEleven.notification.slack.domain.vo.SlackMessageItem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SlackDomainService {

	private static final DateTimeFormatter DATETIME_FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public String formatMessage(SlackMessageContext ctx, String aiMessageBody) {
		StringBuilder sb = new StringBuilder();

		sb.append("주문 번호 : ").append(ctx.orderId()).append("\n");
		sb.append("주문자 정보 : ")
				.append(ctx.customerName())
				.append(" / ")
				.append(ctx.customerEmail())
				.append("\n");
		sb.append("주문 시간 : ").append(formatDateTime(ctx.orderDateTime())).append("\n");
		sb.append("상품 정보 : ").append(formatItems(ctx.items())).append("\n");
		sb.append("요청 사항 : ").append(nullSafe(ctx.requestNote())).append("\n");
		sb.append("발송지 : ").append(ctx.sourceHub()).append("\n");
		sb.append("경유지 : ").append(formatViaHubs(ctx.viaHubs())).append("\n");
		sb.append("도착지 : ")
				.append(ctx.destinationHub())
				.append(" / ")
				.append(ctx.destinationAddress())
				.append("\n");
		sb.append("배송담당자 : ")
				.append(ctx.deliveryManagerName())
				.append(" / ")
				.append(ctx.deliveryManagerEmail())
				.append("\n\n");

		if (aiMessageBody != null && !aiMessageBody.isBlank()) {
			sb.append(aiMessageBody);
		}

		return sb.toString();
	}

	private String formatItems(List<SlackMessageItem> items) {
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
