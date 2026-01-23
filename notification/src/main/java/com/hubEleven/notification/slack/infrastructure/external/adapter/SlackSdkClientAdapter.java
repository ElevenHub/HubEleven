package com.hubEleven.notification.slack.infrastructure.external.adapter;

import com.hubEleven.notification.slack.application.port.SlackClient;
import com.hubEleven.notification.slack.infrastructure.config.SlackSdkConfig;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackSdkClientAdapter implements SlackClient {

	private final Slack slack;
	private final SlackSdkConfig slackSdkConfig;

	private MethodsClient methods() {
		return slack.methods(slackSdkConfig.getBotToken());
	}

	@Override
	public SlackSendResult sendToChannel(String channel, String text) {
		try {
			ChatPostMessageResponse res =
					methods().chatPostMessage(req -> req.channel(channel).text(text));

			if (Boolean.TRUE.equals(res.isOk())) {
				log.info("슬랙 채널 전송 성공 - channel={}, ts={}", res.getChannel(), res.getTs());
				return SlackSendResult.ok(res.getChannel(), res.getTs());
			}

			log.warn("슬랙 채널 전송 실패 - channel={}, error={}", channel, res.getError());
			return SlackSendResult.fail(res.getError());

		} catch (Exception e) {
			log.error("슬랙 채널 전송 중 예외 발생 - channel={}", channel, e);
			return SlackSendResult.fail(e.getMessage());
		}
	}

	@Override
	public SlackSendResult sendDmByEmail(String email, String text) {
		try {
			UsersLookupByEmailResponse userRes = methods().usersLookupByEmail(req -> req.email(email));

			if (!Boolean.TRUE.equals(userRes.isOk()) || userRes.getUser() == null) {
				String error = userRes.getError();
				log.warn("슬랙 유저 조회 실패(이메일) - email={}, error={}", email, error);
				return SlackSendResult.fail(error);
			}

			String userId = userRes.getUser().getId();

			ConversationsOpenResponse openRes =
					methods().conversationsOpen(req -> req.users(List.of(userId)));

			if (!Boolean.TRUE.equals(openRes.isOk()) || openRes.getChannel() == null) {
				String error = openRes.getError();
				log.warn("슬랙 DM 채널 오픈 실패 - email={}, userId={}, error={}", email, userId, error);
				return SlackSendResult.fail(error);
			}

			String channelId = openRes.getChannel().getId();

			ChatPostMessageResponse msgRes =
					methods().chatPostMessage(req -> req.channel(channelId).text(text));

			if (Boolean.TRUE.equals(msgRes.isOk())) {
				log.info(
						"슬랙 DM 전송 성공 - email={}, channelId={}, ts={}",
						email,
						msgRes.getChannel(),
						msgRes.getTs());
				return SlackSendResult.ok(msgRes.getChannel(), msgRes.getTs());
			}

			log.warn(
					"슬랙 DM 전송 실패 - email={}, channelId={}, error={}", email, channelId, msgRes.getError());
			return SlackSendResult.fail(msgRes.getError());

		} catch (Exception e) {
			log.error("슬랙 DM 전송 중 예외 발생 - email={}", email, e);
			return SlackSendResult.fail(e.getMessage());
		}
	}
}
