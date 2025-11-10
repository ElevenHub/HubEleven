package com.hubEleven.hub.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoGeocodingResponse(List<Document> documents, Meta meta) {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Document(
			@JsonProperty("address_name") String addressName,
			@JsonProperty("x") String longitude,
			@JsonProperty("y") String latitude) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Meta(@JsonProperty("total_count") int totalCount) {}

	public boolean hasResult() {
		return documents != null && !documents.isEmpty();
	}

	public Document getFirstDocument() {
		return documents.get(0);
	}
}
