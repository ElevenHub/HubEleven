package com.hubEleven.notification.slack.application.validator;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
import com.hubEleven.notification.slack.domain.repository.SlackMessageRepository;
import com.hubEleven.notification.slack.exception.SlackMessageErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SlackValidator {

    private final SlackMessageRepository slackMessageRepository;

    public void slackCreate(UUID orderId){
        slackMessageRepository.findFirstByOrderIdAndStatus(orderId, SlackMessageStatus.SENT)
                .ifPresent(m -> { throw new GlobalException(SlackMessageErrorCode.SLACK_MESSAGE_ALREADY_SENT); });
    }

    public void slackUpdate() {
        /* TODO: add auth later */
    }

    public void slackRead() {
        /* TODO: add auth later */
    }

    public void slackDelete() {
        /* TODO: add auth later */
    }
}
