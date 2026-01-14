package com.hubEleven.notification.slack.application.service.impl;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.ai.application.dto.response.GenerateMessageResponse;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import com.hubEleven.notification.ai.exception.NotificationErrorCode;
import com.hubEleven.notification.slack.application.command.CreateSlackMessageCommand;
import com.hubEleven.notification.slack.application.command.SearchSlackMessageCommand;
import com.hubEleven.notification.slack.application.command.UpdateSlackMessageCommand;
import com.hubEleven.notification.slack.application.dto.response.SlackMessageResult;
import com.hubEleven.notification.slack.application.service.SlackService;
import com.hubEleven.notification.slack.application.validator.SlackValidator;
import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;
import com.hubEleven.notification.slack.domain.repository.SlackMessageRepository;
import com.hubEleven.notification.slack.domain.service.SlackMessageDomainService;
import com.hubEleven.notification.slack.exception.SlackMessageErrorCode;
import com.hubEleven.notification.slack.infrastructure.client.SlackWebhookClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlackServiceImpl implements SlackService {

    private final SlackMessageRepository slackMessageRepository;
    private final SlackWebhookClient slackWebhookClient;
    private final SlackMessageDomainService slackMessageDomainService;
    private final AiRequestLogRepository aiRequestLogRepository;
    private final ObjectMapper objectMapper;
    private final SlackValidator slackValidator;

    @Override
    @Transactional
    public SlackMessageResult createMessage(CreateSlackMessageCommand command) {
        slackValidator.slackCreate(command.orderId());

        GenerateMessageResponse aiResponse = findAiResultOrThrow(command.orderId());
        String formattedMessage = slackMessageDomainService.formatMessage(command, aiResponse);

        SlackMessage slackMessage =
                SlackMessage.create(
                        command.orderId(),
                        command.recipientId(),
                        command.channel(),
                        formattedMessage);

        SlackMessage saved = slackMessageRepository.save(slackMessage);

        sendToSlackAsync(saved.getId(), formattedMessage);

        return SlackMessageResult.from(saved);
    }

    @Override
    @Transactional
    public SlackMessageResult updateMessage(UpdateSlackMessageCommand command) {
        slackValidator.slackUpdate();

        SlackMessage slackMessage =
                slackMessageRepository
                        .findById(command.messageId())
                        .orElseThrow(() -> new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_NOT_FOUND));

        slackMessage.updateMessage(command.message());
        SlackMessage updated = slackMessageRepository.save(slackMessage);

        return SlackMessageResult.from(updated);
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {
        slackValidator.slackDelete();

        SlackMessage slackMessage =
                slackMessageRepository
                        .findById(messageId)
                        .orElseThrow(() -> new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_NOT_FOUND));

        throw new UnsupportedOperationException("삭제 로직을 연결하세요.");
    }

    @Override
    public SlackMessageResult getMessage(UUID messageId) {
        slackValidator.slackRead();

        SlackMessage slackMessage =
                slackMessageRepository
                        .findById(messageId)
                        .orElseThrow(() -> new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_NOT_FOUND));

        return SlackMessageResult.from(slackMessage);
    }

    @Override
    public CommonPageResponse<SlackMessageResult> searchMessages(
            SearchSlackMessageCommand command,
            CommonPageRequest pageReq) {
        slackValidator.slackRead();

        var page =
                slackMessageRepository.search(
                        command.status(),
                        command.channel(),
                        command.dateFrom(),
                        command.dateTo(),
                        pageReq.toPageable());

        return PagingUtils.convert(page, SlackMessageResult::from);
    }

    private GenerateMessageResponse findAiResultOrThrow(UUID orderId) {
        var logEntry =
                aiRequestLogRepository
                        .findByOrderId(orderId)
                        .orElseThrow(() -> new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL));

        String raw = logEntry.getRawResponse();
        String cleaned = cleanJsonResponse(raw);

        try {
            var payload = objectMapper.readValue(cleaned, ResponsePayload.class);

            if (payload.finalDispatchDeadline == null || payload.finalDispatchDeadline.isBlank()) {
                throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
            }
            if (payload.messageBody == null || payload.messageBody.isBlank()) {
                throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
            }
            return GenerateMessageResponse.success(payload.finalDispatchDeadline, payload.messageBody);
        } catch (Exception e) {
            throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
        }
    }

    private String cleanJsonResponse(String rawJson) {
        if (rawJson == null || rawJson.isBlank()) return rawJson;

        String cleaned = rawJson.trim();

        if (cleaned.startsWith("```json")) cleaned = cleaned.substring(7);
        else if (cleaned.startsWith("```")) cleaned = cleaned.substring(3);

        if (cleaned.endsWith("```")) cleaned = cleaned.substring(0, cleaned.length() - 3);

        return cleaned.trim();
    }

    private void sendToSlackAsync(UUID messageId, String messageText) {
        String title = "배송 예상 시간 알림";

        slackWebhookClient
                .sendMessage(title, messageText)
                .subscribe(
                        ok -> {
                            if (Boolean.TRUE.equals(ok)) {
                                updateMessageStatus(messageId, SlackMessageStatus.SENT);
                                log.info("슬랙 메시지 전송 성공 messageId={}", messageId);
                            } else {
                                updateMessageStatus(messageId, SlackMessageStatus.FAILED);
                                log.error("슬랙 웹훅 응답이 실패 messageId={}", messageId);
                            }
                        },
                        error -> {
                            updateMessageStatus(messageId, SlackMessageStatus.FAILED);
                            log.error("슬랙 메시지 전송 중 오류가 발생 messageId={}", messageId, error);
                        });
    }

    @Transactional
    protected void updateMessageStatus(UUID messageId, SlackMessageStatus status) {
        slackMessageRepository
                .findById(messageId)
                .ifPresent(
                        message -> {
                            if (status == SlackMessageStatus.SENT) {
                                message.markAsSent();
                            } else if (status == SlackMessageStatus.FAILED) {
                                message.markAsFailed();
                            }
                            slackMessageRepository.save(message);
                        });
    }

    private static final class ResponsePayload {
        public String finalDispatchDeadline;
        public String messageBody;
    }
}
