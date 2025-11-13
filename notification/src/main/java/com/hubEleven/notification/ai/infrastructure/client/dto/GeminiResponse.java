package com.hubEleven.notification.ai.infrastructure.client.dto;

import com.commonLib.common.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hubEleven.notification.ai.domain.exception.NotificationErrorCode;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiResponse(List<Candidate> candidates) {

	public String primaryText() {
		if (candidates == null || candidates.isEmpty()) {
			throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
		}

		return candidates.stream()
				.findFirst()
				.flatMap(
						candidate -> {
							Content content = candidate.content();
							if (content == null || content.parts() == null || content.parts().isEmpty()) {
								return java.util.Optional.empty();
							}
							return content.parts().stream()
									.map(Part::text)
									.filter(text -> text != null && !text.isBlank())
									.findFirst();
						})
				.orElseThrow(() -> new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL));
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Candidate(Content content) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Content(String role, List<Part> parts) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Part(String text) {}
}
