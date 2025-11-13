package com.hubEleven.notification.ai.domain.model;

import com.commonLib.common.annotation.SoftDeletable;
import com.commonLib.common.model.BaseEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
		name = "p_ai_request_log",
		indexes = {
			@Index(name = "idx_ai_req_order", columnList = "order_id"),
			@Index(name = "idx_ai_req_status", columnList = "request_status")
		})
@SoftDeletable
@NoArgsConstructor
public class AiRequestLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "ai_request_log_id", nullable = false)
	private UUID id;

	@Column(name = "order_id", nullable = false)
	private UUID orderId;

	@Enumerated(EnumType.STRING)
	@Column(name = "request_status", nullable = false)
	private RequestStatus status;

	@Lob
	@Column(name = "raw_prompt", columnDefinition = "text")
	private String rawPrompt;

	@Lob
	@Column(name = "raw_response", columnDefinition = "text")
	private String rawResponse;

	@Lob
	@Column(name = "metadata_json", columnDefinition = "text")
	private String metadataJson;

	private AiRequestLog(UUID orderId, RequestStatus status, String prompt) {
		this.orderId = orderId;
		this.status = status;
		this.rawPrompt = prompt;
	}

	public static AiRequestLog requested(UUID orderId, String prompt) {
		return new AiRequestLog(orderId, RequestStatus.REQUESTED, prompt);
	}

	public void success(String response, String metadataJson) {
		this.status = RequestStatus.SUCCESS;
		this.rawResponse = response;
		this.metadataJson = metadataJson;
	}

	public void fail(String failurePayload, String metadataJson) {
		this.status = RequestStatus.FAIL;
		this.rawResponse = failurePayload;
		this.metadataJson = metadataJson;
	}
}
