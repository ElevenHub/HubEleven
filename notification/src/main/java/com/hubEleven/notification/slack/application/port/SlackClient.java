package com.hubEleven.notification.slack.application.port;

public interface SlackClient {

    SlackSendResult sendToChannel(String channel, String text);

    SlackSendResult sendDmByEmail(String email, String text);

    record SlackSendResult(boolean success, String channelId, String ts, String error) {
        public static SlackSendResult ok(String channelId, String ts) {
            return new SlackSendResult(true, channelId, ts, null);
        }
        public static SlackSendResult fail(String error) {
            return new SlackSendResult(false, null, null, error);
        }
    }
}
