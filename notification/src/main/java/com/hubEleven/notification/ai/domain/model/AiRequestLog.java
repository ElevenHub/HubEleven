package com.hubEleven.notification.ai.domain.model;

import com.commonLib.common.model.BaseEntity;
import com.hubEleven.notification.ai.domain.vo.RequestStatus;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
		name = "p_ai_request_log",
		uniqueConstraints = {@UniqueConstraint(name = "uk_ai_req_order", columnNames = "order_id")},
		indexes = {
			@Index(name = "idx_ai_req_order", columnList = "order_id"),
			@Index(name = "idx_ai_req_status", columnList = "request_status")
		})
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

	@Column(name = "final_dispatch_deadline", length = 255)
	private String finalDispatchDeadline;

	@Lob
	@Column(name = "message_body", columnDefinition = "text")
	private String messageBody;

	private AiRequestLog(UUID orderId, RequestStatus status, String prompt, String metadataJson) {
		this.orderId = orderId;
		this.status = status;
		this.rawPrompt = prompt;
		this.metadataJson = metadataJson;
	}

	public static AiRequestLog requested(UUID orderId, String prompt, String metadataJson) {
		return new AiRequestLog(orderId, RequestStatus.REQUESTED, prompt, metadataJson);
	}

	public AiRequestLog success(
			String finalDispatchDeadline, String messageBody, String rawResponse, String metadataJson) {
		this.status = RequestStatus.SUCCESS;
		this.finalDispatchDeadline = finalDispatchDeadline;
		this.messageBody = messageBody;
		this.rawResponse = rawResponse;
		this.metadataJson = metadataJson;
		return this;
	}

	public AiRequestLog fail(String failurePayload, String metadataJson) {
		this.status = RequestStatus.FAIL;
		this.rawResponse = failurePayload;
		this.metadataJson = metadataJson;
		return this;
	}
}
