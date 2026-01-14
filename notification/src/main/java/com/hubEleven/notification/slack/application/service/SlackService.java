package com.hubEleven.notification.slack.application.service;

import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.notification.slack.application.command.CreateSlackMessageCommand;
import com.hubEleven.notification.slack.application.command.SearchSlackMessageCommand;
import com.hubEleven.notification.slack.application.command.UpdateSlackMessageCommand;
import com.hubEleven.notification.slack.application.dto.response.SlackMessageResult;

import java.util.UUID;

public interface SlackService {
	SlackMessageResult createMessage(CreateSlackMessageCommand command);

	SlackMessageResult updateMessage(UpdateSlackMessageCommand command);

	void deleteMessage(UUID messageId);

	SlackMessageResult getMessage(UUID messageId);

	CommonPageResponse<SlackMessageResult> searchMessages(
			SearchSlackMessageCommand command,
			CommonPageRequest pageReq
	);
}
